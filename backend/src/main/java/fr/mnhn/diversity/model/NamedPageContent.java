package fr.mnhn.diversity.model;

import java.util.Map;
import java.util.Objects;

/**
 * An association of a page name and its content. This is useful when an HTML page needs to embed part of another page
 * and link to it. For example, the ecogesture home page displays a list of gestures. Each gesture is in instance of
 * the ecogesture page content, and only a small part of each gesture is displayed. To link to the details of the gesture,
 * we need the name of the page in addition to its content, in order to compose the URL.
 * @author JB Nizet
 */
public final class NamedPageContent {
    private final String pageName;
    private final Map<String, Object> content;

    public NamedPageContent(String pageName, Map<String, Object> content) {
        this.pageName = pageName;
        this.content = content;
    }

    public String getPageName() {
        return pageName;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NamedPageContent)) {
            return false;
        }
        NamedPageContent that = (NamedPageContent) o;
        return Objects.equals(pageName, that.pageName) &&
            Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageName, content);
    }

    @Override
    public String toString() {
        return "NamedPageContent{" +
            "pageName=" + pageName +
            ", model=" + content +
            '}';
    }
}
