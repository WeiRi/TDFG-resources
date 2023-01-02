package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/*** ��� Ȯ�� ***/

public class ViewTree {
	private final static String _cross = " ├─";
    private final static String _corner = " └─";
    private final static String _vertical = " │ ";
    private final static String _space = "   ";
    

    //실행 후, 간략하게 정리, 불필요한 부분 제거, 가지치기 하는 부분 명확하게 시각
    //html 큰 가지만 보일 수 있게
    
    private ViewTree() {}
    
    public static void makeViewTreeFile(Document doc, String fileName) {
        File file = new File(fileName);
        try {
        	
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            doc.traverse(new NodeVisitor() {
                public void head(Node node, int depth) {
                	if(node.nodeName()=="html"|| node.nodeName()=="head" || node.nodeName() == "title" 
                			||node.nodeName()=="body"||node.nodeName()=="p"|| node.nodeName() == "h1" 
                			||node.nodeName()=="h2"||node.nodeName()=="h3"||node.nodeName()=="h4"
                			||node.nodeName()=="h5"||node.nodeName()=="h6"||node.nodeName()=="div" ||node.nodeName()=="a"){
                		
                		try {
                        	for(int i=0; i<depth-1; i++) {
                        		bw.write(_space);
                        	}
                        	if(depth > 1) {
                        		bw.write(_cross);
                        	}
                        	
                        	if(node.hasAttr("class")) {
                        		bw.write(node.nodeName()+ "\t" + "class=" + node.attr("class") +"\n");
                        	}else if(node.hasAttr("id")){
                        		bw.write(node.nodeName()+ "\t" + "id=" + node.attr("id")+"\n");
                        	}else {
                        		bw.write(node.nodeName()+"\n");
                        	}
                        	
                        	//id랑 class 이름까지 추출 
                    		System.out.println("Entering tag: " + node.nodeName());
                        	
            			} catch (IOException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}
                		
                	}
                
                }
                public void tail(Node node, int depth) {
//                    System.out.println("Exiting tag: " + node.nodeName());
                }
            });
            
            bw.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
    }
}