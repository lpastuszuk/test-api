package qa.task.context;

import io.restassured.path.xml.XmlPath;

public class TestContext {
    private XmlPath xmlPath;

    public XmlPath getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(XmlPath xmlPath) {
        this.xmlPath = xmlPath;
    }
}
