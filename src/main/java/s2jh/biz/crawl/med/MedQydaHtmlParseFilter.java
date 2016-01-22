package s2jh.biz.crawl.med;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class MedQydaHtmlParseFilter extends MedBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.3618med.com/company/introduce/[0-9]{0,}.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);
        {
            NodeList nodes = selectNodeList(df, "//DIV[@class='com_left_bor noTbor noBbor']//DL");
            if (nodes != null && nodes.getLength() > 0) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    String nodeText = nodes.item(i).getTextContent();
                    //中文冒号分隔
                    String[] nodeTexts = StringUtils.split(nodeText, "：");
                    //容错处理，英文冒号分隔
                    if (nodeTexts.length <= 1) {
                        nodeTexts = StringUtils.split(nodeText, ":");
                    }
                    if (nodeTexts.length > 1) {
                        putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                    }
                }
            }

            //邮箱处理
            String emailSalt = getXPathAttribute(df, "/html/body/div[9]/div[1]/div[1]/div[3]/div[2]/div[2]/dl[5]/dd/a", "data-cfemail");
            String email = decryptEmail(emailSalt);
            putKeyValue(parsedDBObject, "企业邮箱", email);
        }
        return parsedDBObject;
    }

    private String decryptEmail(String a) {
        StringBuffer e = new StringBuffer();
        if (StringUtils.isNotBlank(a)) {
            int r = Integer.parseInt(a.substring(0, 2), 16);
            for (int n = 2; a.length() - n > 0; n += 2) {
                int i = Integer.parseInt(a.substring(n, n + 2), 16) ^ r;
                e.append((char) i);
            }
        }
        return e.toString();
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://www.3618med.com/company/introduce/"), ".html");
    }
}
