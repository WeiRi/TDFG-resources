package online.pizzacrust.roblox.impl;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import online.pizzacrust.roblox.Robloxian;
import online.pizzacrust.roblox.group.Group;
import online.pizzacrust.roblox.group.Roleset;

public class BasicGroup implements Group{

    private final int groupId;

    public BasicGroup(int groupId) {
        this.groupId = groupId;
    }

    public static class RolesetData {
        public int Id;
        public String Name;
        public int Rank;
    }

    @Override
    public List<Roleset> getRolesets() throws Exception {
        List<Roleset> rolesets = new ArrayList<>();
        String url = "https://www.roblox.com/api/groups/" + groupId + "/RoleSets/";
        RolesetData[] data = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true).get()
                .body().text(), RolesetData[].class);
        for (RolesetData datum : data) {
            rolesets.add(new BasicRoleset(datum.Rank, datum.Id, datum.Name));
        }
        return rolesets;
    }

    public static class NameData {
        public String Name;
    }

    @Override
    public String getName() throws Exception {
        String url = "https://api.roblox.com/groups/" + groupId;
        NameData data = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true).get().body
                ().text(), NameData.class);
        return data.Name;
    }

    private Robloxian.LightReference[] recursiveRetrieve(Roleset roleset, String cursor) throws IOException {
        String url = "https://groups.roblox" +
                ".com/v1/groups/" + groupId + "/roles/" + roleset.getId() +
                "/users?sortOrder=Asc&limit=100&cursor=" + cursor;
        PlayersData data = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true).get()
                .body().text(), PlayersData.class);
        List<Robloxian.LightReference> references = new ArrayList<>();
        for (PlayersData.PlayerData datum : data.data) {
            references.add(new BasicReference(datum.userId, datum.username));
        }
        if (data.nextPageCursor != null) {
            Collections.addAll(references, recursiveRetrieve(roleset, data.nextPageCursor));
        }
        return references.toArray(new Robloxian.LightReference[references.size()]);
    }

    @Override
    public Robloxian.LightReference[] getMembersInRole(Roleset roleset) throws Exception {
        String url = "https://groups.roblox" +
                ".com/v1/groups/" + groupId + "/roles/" + roleset.getId() +
        "/users?sortOrder=Asc&limit=100";
        PlayersData data = new Gson().fromJson(Jsoup.connect(url).ignoreContentType(true).get()
                .body().text(), PlayersData.class);
        List<Robloxian.LightReference> references = new ArrayList<>();
        for (PlayersData.PlayerData datum : data.data) {
            references.add(new BasicReference(datum.userId, datum.username));
        }
        if (data.nextPageCursor != null) {
            Collections.addAll(references, recursiveRetrieve(roleset, data.nextPageCursor));
        }
        return references.toArray(new Robloxian.LightReference[references.size()]);
    }

    @Override
    public Optional<Roleset> getRole(Robloxian robloxian) throws Exception {
        String url = "https://www.roblox.com/Game/LuaWebService/HandleSocialRequest.ashx" +
                "?method=GetGroupRank" +
                "&playerid=" + robloxian.getUserId() +
                "&groupid=" + this.groupId;
        int rankIndex = Integer.parseInt(Jsoup.connect(url).ignoreContentType(true).get().body()
                .text());
        List<Roleset> rolesets = getRolesets();
        for (Roleset roleset : rolesets) {
            if (roleset.getRankIndex() == rankIndex) {
                return Optional.of(roleset);
            }
        }
        return Optional.empty();
    }

    @Override
    public int getId() {
        return groupId;
    }

    private String index(int i) {
        String iS = "" + i;
        if (iS.toCharArray().length == 1) {
            return "0" + iS;
        }
        return iS;
    }

    // __eventtarget, scriptmanager, eventvalidation, viewstate, viewstategenerator
    private Map<String, String> getRequiredFormData(int nextIndex,
                                                    Document currentPage) {
        HashMap<String, String> formDataMap = new HashMap<>();
        formDataMap.put("ctl00$ScriptManager",
                "ctl00$cphRoblox$rbxGroupAlliesPane$RelationshipsUpdatePanel" +
                "|ctl00$cphRoblox$rbxGroupAlliesPane$RelationshipsDataPager$ctl00$ctl" + index
                        (nextIndex));
        formDataMap.put("__EVENTTARGET",
                "ctl00$cphRoblox$rbxGroupAlliesPane$RelationshipsDataPager$ctl00$ctl" + index(nextIndex));
        formDataMap.put("__EVENTVALIDATION", currentPage.getElementById("__EVENTVALIDATION").attr
                ("value"));
        formDataMap.put("__VIEWSTATEGENERATOR", currentPage.getElementById
                ("__VIEWSTATEGENERATOR").attr("value"));
        formDataMap.put("__VIEWSTATE", currentPage.getElementById("__VIEWSTATE").attr("value"));
        return formDataMap;
    }

    private boolean moreAllyPages(Document document) {
        Element root = document.getElementById
                ("ctl00_cphRoblox_rbxGroupAlliesPane_RelationshipsDataPager");
        for (Element a : root.getElementsByTag("a")) {
            if (a.text().equalsIgnoreCase("Next")) {
                if (!a.hasAttr("disabled")) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    private List<Group> parsePage(Document document) {
        Element root = document.getElementsByClass("grouprelationshipscontainer").first();
        List<Group> groups = new ArrayList<>();
        for (Element div : root.getElementsByTag("div")) {
            if (div.attr("style").equalsIgnoreCase("width:42px;height:42px;padding:8px;" +
                    "float:left")) {
                groups.add(new BasicGroup(Integer.parseInt(div.getElementsByTag("a").first().attr
                        ("href").split
                        ("=")[1])));
            }
        }
        return groups;
    }

    private List<Group> recursiveGetAllies(int nIndex, Document prevPage) throws IOException {
        Map<String, String> form = getRequiredFormData(nIndex, prevPage);
        String url = "https://www.roblox.com/Groups/Group.aspx?gid=" + groupId;
        Document document = Jsoup.connect(url).data(form).post();
        List<Group> groups = new ArrayList<>(parsePage(document));
        if (moreAllyPages(document)) {
            groups.addAll(recursiveGetAllies(nIndex + 1, document));
        }
        return groups;
    }

    @Override
    public List<Group> getAllies() throws Exception {
        String url = "https://www.roblox.com/Groups/Group.aspx?gid=" + groupId;
        Document document = Jsoup.connect(url).get();
        List<Group> groups = new ArrayList<>(parsePage(document));
        if (moreAllyPages(document)) {
            // recursive method
            groups.addAll(recursiveGetAllies(1, document));
        }
        return groups;
    }

    public static void main(String... args) throws Exception {
        BasicGroup basicGroup = new BasicGroup(2900057);
        for (Group group : basicGroup.getAllies()) {
            System.out.println(group.getName() + "#" + group.getId()
            );
        }
        System.out.println("Total: " + basicGroup.getAllies().size());
    }

    public static class PlayersData {
        public static class PlayerData {
            public int userId;
            public String username;
        }

        public String nextPageCursor;
        public PlayerData[] data;
    }

}
