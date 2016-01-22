package s2jh.biz.crawl.med;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class MedAboutUsHtmlParseFilter extends MedBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://[a-zA-z0-9]{0,}.3618med.com/sub/zh-CN/aboutus.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);
        //关于我们 两个模板
        Node node = selectSingleNode(df, "//DIV[@class='menu']");
        if (node != null) {
            {
                NodeList nodes = selectNodeList(df, "//DIV[@class='c-proB']/UL/LI");
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
                String emailSalt = getXPathAttribute(df, "/html/body/div/div[6]/div[2]/div[2]/div/ul/li[8]/span[2]/a", "data-cfemail");
                String email = decryptEmail(emailSalt);
                putKeyValue(parsedDBObject, "邮箱", email);

            }
            putKeyValue(parsedDBObject, "主营产品", getXPathValue(df, "/html/body/div/div[6]/div[1]/div[14]/div/ul/li"));
        } else {
            {
                NodeList nodes = selectNodeList(df, "/html/body/div/table[4]/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr");
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
                String emailSalt = getXPathAttribute(df,
                        "/html/body/div/table[4]/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr[7]/td[2]/a", "data-cfemail");
                String email = decryptEmail(emailSalt);
                putKeyValue(parsedDBObject, "邮箱", email);

            }
            putKeyValue(parsedDBObject, "主营产品",
                    getXPathValue(df, "/html/body/div/table[4]/tbody/tr/td[1]/table/tbody/tr[8]/td/table/tbody/tr/td[2]/span"));
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
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://"), ".3618med.com/sub/zh-CN/aboutus.html");
    }
}
