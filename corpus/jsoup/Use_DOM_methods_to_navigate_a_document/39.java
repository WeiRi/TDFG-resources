package online.pizzacrust.roblox.impl;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import online.pizzacrust.roblox.Roblox;
import online.pizzacrust.roblox.Robloxian;
import online.pizzacrust.roblox.forum.ForumResponse;
import online.pizzacrust.roblox.forum.ForumThread;
import online.pizzacrust.roblox.group.Group;

public class BasicForumThread implements ForumThread {

    private final int id;

    public BasicForumThread(int id) throws Exception {
        this.id = id;
    }

    @Override
    public List<ForumResponse> getForumResponses() throws Exception {
        String url = "https://forum.roblox.com/Forum/ShowPost.aspx?PostID=" + id;
        // first is get, second is post and the rest
        // first u need to get __VIEWSTATE, __VIEWSTATEGENERATOR, __EVENTVALIDATION
        Document document = Jsoup.connect(url).ignoreContentType(true).get();
        String viewStateGenerator = (document.getElementById("__VIEWSTATEGENERATOR").attr
                ("value"));
        String eventValidation = (document.getElementById("__EVENTVALIDATION").attr("value"));
        String viewState = (document.getElementById("__VIEWSTATE").attr("value"));
        // check if there are more pages
        if (document.getElementById("ctl00_cphRoblox_PostView1_ctl00_Pager_Next")
                 == null) {
            System.out.println("No more pages");
        }
        // now post to the link w/ __viewstate, __viewstategenerator, __eventvalidation, and also
        // target
        //Document otherDoc = Jsoup.connect(url).ignoreContentType(true).data
        //        ("__VIEWSTATEGENERATOR", viewStateGenerator).data("__VIEWSTATE", viewState)
        //        .data("__EVENTVALIDATION", eventValidation).data("__EVENTTARGET",
        //        "ctl00$cphRoblox$PostView1$ctl00$Pager$Next").post();
        //System.out.println(otherDoc.body());
        List<Document> documents = recursiveGet(viewState, eventValidation, viewStateGenerator,
                url);
        documents.add(document);
        return process(documents);
    }

    private String getUsername(Element element) {
        Elements elements = element.getElementsByTag("a");
        for (Element element1 : elements) {
            if (element1.attr("href").startsWith("https://www.roblox.com/users/")) {
                return element1.text();
            }
        }
        throw new RuntimeException();
    }

    private List<ForumResponse> process(List<Document> documents) {
        List<ForumResponse> forumResponses = new ArrayList<>();
        for (Document document : documents) {
            for (Element element : document.getElementsByClass("forum-post")) {
                BasicForumResponse response = new BasicForumResponse(getUsername(element),
                        element.getElementsByClass("normalTextSmall").first().text());
                forumResponses.add(response);
            }
        }
        return forumResponses;
    }

    private List<Document> recursiveGet(String viewState, String eventValidation, String
            viewStateGenerator, String url) throws Exception {
        List<Document> documents = new ArrayList<>();
        Document document = Jsoup.connect(url).ignoreContentType(true).data
                ("__VIEWSTATEGENERATOR", viewStateGenerator).data("__VIEWSTATE", viewState)
                .data("__EVENTVALIDATION", eventValidation).data("__EVENTTARGET", "ctl00$cphRoblox$PostView1$ctl00$Pager$Next").post();
        documents.add(document);
        if (document.getElementById("ctl00_cphRoblox_PostView1_ctl00_Pager_Next")
                == null) {
            return documents;
        }
        String nviewStateGenerator = (document.getElementById("__VIEWSTATEGENERATOR").attr
                ("value"));
        String neventValidation = (document.getElementById("__EVENTVALIDATION").attr("value"));
        String nviewState = (document.getElementById("__VIEWSTATE").attr("value"));
        documents.addAll(recursiveGet(nviewState, neventValidation, nviewStateGenerator, url));
        return documents;
    }

    /*
    public static class ProcessAlts implements Callable<List<ForumResponse>> {

        private final List<String> errors;
        private final List<ForumResponse> responses;

        public ProcessAlts(List<String> errors, List<ForumResponse> responses) {
            this.responses = responses;
            this.errors =errors;
        }

        @Override
        public List<ForumResponse> call() throws Exception {
            // returns credible
            List<ForumResponse> credibleResponses = new ArrayList<>();
            Group group = new BasicGroup(758071);
            int i = 0;
            for (ForumResponse response : responses) {
                System.out.println(i + "/" + (responses.size() - 1));
                Robloxian robloxian = Roblox.get(response.getUsername()).get();
                if (robloxian.getJoinTimeInDays() >= 90) {
                    if (group.getRole(robloxian).isPresent()) {
                      credibleResponses.add(response);
                    } else {
                        errors.add("REMOVED - " + response.getUsername() + " - GROUP ");
                    }
                } else {
                    errors.add("REMOVED - " + response.getUsername() + " - UNDERAGE");
                }
                i++;
            }
            return credibleResponses;
        }
    }

    public static void main(String... args) throws Exception {
        int large   = 0;
        int acid    = 0;
        int tricor  = 0;
        List<ForumResponse> unpartitioned = new BasicForumThread(228341924).getForumResponses();
        List<List<ForumResponse>> partitioned = Lists.partition(unpartitioned, 6);
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future<List<ForumResponse>>> futures = new ArrayList<>();
        List<String> strings = Collections.synchronizedList(new ArrayList<>());
        for (List<ForumResponse> forumResponses : partitioned) {
            futures.add(executorService.submit(new ProcessAlts(strings, forumResponses)));
        }
        while (((ThreadPoolExecutor) executorService).getActiveCount() != 0) { }
        List<ForumResponse> credibileSources = new ArrayList<>();
        for (Future<List<ForumResponse>> future : futures) {
            credibileSources.addAll(future.get());
        }
        List<String> votedUsers = new ArrayList<>();
        for (ForumResponse forumResponse : credibileSources) {
            if (!votedUsers.contains(forumResponse.getUsername())) {
                if (forumResponse.getMessage().toLowerCase().contains("ohr")) {
                    large++;
                    votedUsers.add(forumResponse.getUsername());
                } else if (forumResponse.getMessage().toLowerCase().contains("tec")) {
                    acid++;
                    votedUsers.add(forumResponse.getUsername());
                } else if (forumResponse.getMessage().toLowerCase().contains("other")) {
                    tricor++;
                    votedUsers.add(forumResponse.getUsername());
                }
            } else {
                strings.add("REMOVED - " + forumResponse.getUsername() + " - DUPLICATE");
            }
        }
        System.out.println("Total: " + (large + acid + tricor));
        System.out.println("Technozo: " + acid);
        System.out.println("Ohriginal: " + large);
        System.out.println("Other: " + tricor);
        executorService.shutdown();
        for (String string : strings) {
            System.out.println(string);
        }
    }
    */

    public static class Test {
        public static void main(String... args) throws Exception {
            List<String> strings = new ArrayList<>();
            for (ForumResponse forumResponse : new BasicForumThread(228223300).getForumResponses
                    ()) {
                if (!strings.contains(forumResponse.getUsername())) {
                    strings.add(forumResponse.getUsername());
                    System.out.println(forumResponse.getMessage());
                }
            }
        }
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
