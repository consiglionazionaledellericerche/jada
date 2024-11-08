/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.jada.util.jsp;

import it.cnr.jada.util.Introspector;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.Properties;

public class Button implements Serializable, Cloneable {

    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    private static final long serialVersionUID = 1L;
    private String accessKey;
    private String img;
    private String disabledImg;
    private String label;
    private String style;
    private String href;
    private String enabledProperty;
    private String hiddenProperty;
    private String name;
    private String iconClass;
    private String buttonClass;

    private boolean separator;
    private String title;
    private int labelPosition;

    public static final String INPUT_FILE = "file-selector";

    public Button() {
        labelPosition = 1;
    }

    public Button(Properties properties, String s) {
        name = s;
        labelPosition = 1;
        img = properties.getProperty(s + ".img");
        disabledImg = properties.getProperty(s + ".disabledImg");
        label = properties.getProperty(s + ".label");
        href = properties.getProperty(s + ".href");
        style = properties.getProperty(s + ".style");
        enabledProperty = properties.getProperty(s + ".enabledProperty");
        hiddenProperty = properties.getProperty(s + ".hiddenProperty");
        title = properties.getProperty(s + ".title");
        accessKey = properties.getProperty(s + ".accessKey");
        iconClass = properties.getProperty(s + ".iconClass");
        buttonClass = properties.getProperty(s + ".buttonClass");
        String s1 = properties.getProperty(s + ".labelPosition");
        if (s1 == null || "bottom".equalsIgnoreCase(s1))
            labelPosition = 1;
        else if ("top".equalsIgnoreCase(s1))
            labelPosition = 0;
        else if ("left".equalsIgnoreCase(s1))
            labelPosition = 2;
        else if ("right".equalsIgnoreCase(s1))
            labelPosition = 3;
        String s2 = properties.getProperty(s + ".separator");
        separator = s2 != null && Boolean.valueOf(s2).booleanValue();
    }

    public static void write(JspWriter jspwriter, String s, String s1, boolean isBootstrap)
            throws IOException {
        write(jspwriter, s, null, s1, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String s, String s1, String s2, boolean isBootstrap)
            throws IOException {
        write(jspwriter, s, s1, s2, null, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String name, String img, String label, int layoutImg, String href, String buttonStyle, String s5, boolean isBootstrap)
            throws IOException {
        write(jspwriter, name, img, label, layoutImg, href, buttonStyle, s5, null, isBootstrap);
    }

    public static void writeWhithAccessKey(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, String accessKey, boolean isBootstrap)
            throws IOException {
        writeWhithAccessKey(jspwriter, s, s1, s2, i, s3, s4, s5, null, accessKey, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String name, String image, String s2, int layoutImg, String href, String buttonStyle, String title,
                             String s6, boolean isBootstrap)
            throws IOException {
        write(jspwriter, name, image, image != null, s2, layoutImg, href, buttonStyle, title, s6, isBootstrap);
    }

    public static void writeWhithAccessKey(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5,
                                           String s6, String accessKey, boolean isBootstrap)
            throws IOException {
        write(jspwriter, s, s1, s1 != null, s2, i, s3, s4, s5, s6, accessKey, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5,
                             String s6, boolean flag, boolean isBootstrap)
            throws IOException {
        if (flag)
            write(jspwriter, null, s, s2, i, s3, s4, s5, s6, isBootstrap);
        else
            write(jspwriter, null, s1, s2, i, null, s4, s5, s6, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5,
                             boolean flag, boolean isBootstrap)
            throws IOException {
        if (flag)
            write(jspwriter, null, s, s2, i, s3, s4, s5, isBootstrap);
        else
            write(jspwriter, null, s1, s2, i, null, s4, s5, isBootstrap);
    }

    public static void writeWithAccessKey(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, String s6, String accesskey, boolean isBootstrap)
            throws IOException {
        write(jspwriter, s, s1, s1 != null, s2, i, s3, s4, s5, s6, accesskey, isBootstrap);
    }

    public static void writeWithAccessKey(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, String accesskey, boolean isBootstrap)
            throws IOException {
        writeWithAccessKey(jspwriter, s, s1, s2, i, s3, s4, s5, null, accesskey, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5,
                             boolean flag, String accesskey, boolean isBootstrap)
            throws IOException {
        if (flag)
            writeWithAccessKey(jspwriter, null, s, s2, i, s3, s4, s5, accesskey, isBootstrap);
        else
            writeWithAccessKey(jspwriter, null, s1, s2, i, null, s4, s5, accesskey, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String img, String label, String href, String s3, boolean isBootstrap)
            throws IOException {
        write(jspwriter, null, img, label, 1, href, s3, null, isBootstrap);
    }

    public static void writeWhithAccessKey(JspWriter jspwriter, String s, String s1, String s2, String s3, String accessKey, boolean isBootstrap)
            throws IOException {
        writeWhithAccessKey(jspwriter, null, s, s1, 1, s2, s3, null, accessKey, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String s, String s1, String s2, String s3, String s4, boolean isBootstrap)
            throws IOException {
        write(jspwriter, s, s1, s2, 1, s3, s4, null, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String s, String s1, String s2, String s3, String s4, String s5, boolean flag, boolean isBootstrap)
            throws IOException {
        if (flag)
            write(jspwriter, null, s, s2, 1, s3, s4, s5, isBootstrap);
        else
            write(jspwriter, null, s1, s2, 1, null, s4, s5, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String s, String s1, String s2, String s3, String s4, boolean flag, boolean isBootstrap)
            throws IOException {
        if (flag)
            write(jspwriter, s, s2, s3, s4, isBootstrap);
        else
            write(jspwriter, s1, s2, null, s4, isBootstrap);
    }

    public static void write(JspWriter jspwriter, String s, String s1, String s2, String s3, boolean flag, boolean isBootstrap)
            throws IOException {
        if (flag)
            write(jspwriter, s, s2, s3, isBootstrap);
        else
            write(jspwriter, s1, s2, null, isBootstrap);
    }

    /* Metodo inserito da Marco Spasiano per gestire l'accessKey*/
    public static void write(JspWriter jspwriter, String name, String image, boolean flag, String label, int layoutImg,
                             String href, String buttonStyle,
                             String title, String s6, String accessKey, boolean isBootstrap) throws IOException {
        boolean isFileButton = Optional.ofNullable(buttonStyle).filter(s -> s.contains("btn-file")).isPresent();
        jspwriter.print(isFileButton ? "<label" :"<button");
        if (flag && !isBootstrap) {
            if (href == null)
                jspwriter.print(" class=\"DisabledButton\"");
            else
                jspwriter.print(" class=\"Button\"");
        }

        if (href == null)
            jspwriter.print(" disabled");

        if (accessKey != null) {
            jspwriter.print(" accessKey=\"");
            jspwriter.print(accessKey);
            jspwriter.print('"');
        }
        if (name != null) {
            jspwriter.print(" name=\"");
            jspwriter.print(name);
            jspwriter.print('"');
        }
        if (href != null && href.startsWith("javascript:"))
            href = href.substring(11);

        if (buttonStyle != null) {
            if (!isBootstrap) {
                jspwriter.print(" style=\"");
                jspwriter.print(buttonStyle);
                jspwriter.print('"');
            } else {
                jspwriter.print(" class=\" ");
                if (!buttonStyle.contains("page-link"))
                    jspwriter.print("btn ");
                jspwriter.print(buttonStyle);
                jspwriter.print('"');
            }
        } else {
            if (isBootstrap) {
                jspwriter.print(" class=\"btn btn-outline-secondary ");
                if (image == null) {
                    jspwriter.print(" btn-sm text-primary");
                } else {
                    if (image.startsWith("img")) {
                        // Workaround per i bottoni dove non è definita una icona
                        jspwriter.print(" btn-outline-primary ");
                        if (label != null) {
                            jspwriter.print(" btn-title ");
                            label = label.replace("<br>", " ").replace("<BR>", " ");
                        } else {
                            jspwriter.print(" btn-sm ");
                        }
                        image = "fa fa-fw fa-share";
                    }
                }
                jspwriter.print("\"");
            }
        }
        if (href != null && !isFileButton) {
            jspwriter.print(" onclick=\"cancelBubble(event); if (disableDblClick()) ");
            jspwriter.print(href);
            jspwriter.print("; return false\"");
        }
        if (flag && !isBootstrap)
            jspwriter.print(" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" onMouseDown=\"mouseDown(this)\" onMouseUp=\"mouseUp(this)\"");

        if (isBootstrap) {
            jspwriter.print(" data-toggle=\"tooltip\" data-placement=\"top\" ");
        }

        if (title != null && !isBootstrap) {
            jspwriter.print(" title=\"");
            jspwriter.print(title);
            jspwriter.print('"');
        }
        if (s6 != null) {
            jspwriter.print(' ');
            jspwriter.print(s6);
        }
        if (isBootstrap) {
            jspwriter.print(" title=\"");
            jspwriter.print(Optional.ofNullable(title).orElse(Optional.ofNullable(label).orElse("")));
            jspwriter.print("\"");
        }
        jspwriter.print(">");
        if (href != null && isFileButton) {
            jspwriter.print("<input name=\"" + INPUT_FILE + "\" type=\"file\" style=\"display:none\" ");
            jspwriter.print(" onchange=\"cancelBubble(event); if (disableDblClick()) ");
            jspwriter.print(href);
            jspwriter.print("; return false\"");
            jspwriter.print(">");
        }
        if (image != null) {
            if (!isBootstrap) {
                jspwriter.print("<img align=\"middle\" class=\"Button\" src=\"");
                jspwriter.print(image);
                jspwriter.print("\">");
                if (label != null) {
                    switch (layoutImg) {
                        case 2: // '\002'
                            jspwriter.print(" ");
                            break;

                        case 3: // '\003'
                            jspwriter.print(" ");
                            break;

                        case 1: // '\001'
                            jspwriter.print("<br>");
                            break;

                        default:
                            jspwriter.print("<br>");
                            break;

                        case 0: // '\0'
                            break;
                    }
                }
            } else {
                jspwriter.println("<i class=\"");
                jspwriter.print(image);
                jspwriter.print("\" aria-hidden=\"true\"></i>");
            }
        } else {
            /**
             * Serve per scrivere i numeri di pagina nella toolbar
             */
            if (isBootstrap) {
                jspwriter.print(label);
            }
        }
        if (label != null && !isBootstrap)
            jspwriter.print(label);

        jspwriter.print(isFileButton ? "</label>" :"</button>");

    }

    public static void write(JspWriter jspwriter, String name, String image, boolean flag, String s2, int layoutImg, String s3, String buttonStyle,
                             String s5, String s6, boolean isBootstrap)
            throws IOException {
        write(jspwriter, name, image, flag, s2, layoutImg, s3, buttonStyle, s5, s6, null, isBootstrap);
    }

    public String getDisabledImg() {
        return disabledImg;
    }

    public void setDisabledImg(String s) {
        disabledImg = s;
    }

    public String getEnabledProperty() {
        return enabledProperty;
    }

    public void setEnabledProperty(String s) {
        enabledProperty = s;
    }

    public String getHiddenProperty() {
        return hiddenProperty;
    }

    public void setHiddenProperty(String s) {
        hiddenProperty = s;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String s) {
        href = s;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String s) {
        img = s;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String s) {
        label = s;
    }

    public int getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(int i) {
        labelPosition = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String s) {
        style = s;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String s) {
        title = s;
    }

    public boolean hasSeparator() {
        return separator;
    }

    public boolean isHidden(Object obj) {
        return Introspector.getBoolean(obj, hiddenProperty, false);
    }

    public void setSeparator(boolean flag) {
        separator = flag;
    }

    /**
     * @return
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * @param string
     */
    public void setAccessKey(String string) {
        accessKey = string;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getButtonClass() {
        return buttonClass;
    }

    public void setButtonClass(String buttonClass) {
        this.buttonClass = buttonClass;
    }

    public void write(JspWriter jspwriter, Object obj, boolean isBootstrap)
            throws IOException {
        if (!Introspector.getBoolean(obj, hiddenProperty, false))
            write(jspwriter,
                    Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(img),
                    Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(disabledImg),
                    label,
                    labelPosition,
                    href,
                    isBootstrap ? buttonClass : style,
                    title,
                    enabledProperty == null || Introspector.getBoolean(obj, enabledProperty, false),
                    accessKey,
                    isBootstrap);
    }

    public void write(JspWriter jspwriter, boolean flag, boolean isBootstrap)
            throws IOException {
        write(jspwriter, Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(img),
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(disabledImg), label, labelPosition, href,
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> buttonClass).orElse(style),
                title, flag, isBootstrap);
    }

    public void write(JspWriter jspwriter, boolean flag, String s, boolean isBootstrap, String addButtonClass)
            throws IOException {
        write(jspwriter, Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(img),
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(disabledImg), label, labelPosition, s,
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> buttonClass.concat(addButtonClass)).orElse(style), title, flag, isBootstrap);
    }

    public void write(JspWriter jspwriter, boolean flag, String s, boolean isBootstrap)
            throws IOException {
        write(jspwriter, Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(img),
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(disabledImg), label, labelPosition, s,
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> buttonClass).orElse(style), title, flag, isBootstrap);
    }

    public void write(JspWriter jspwriter, boolean flag, String s, String s1, boolean isBootstrap)
            throws IOException {
        write(jspwriter, Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(img),
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(disabledImg), label, labelPosition, s,
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> buttonClass).orElse(style), title, s1, flag, isBootstrap);
    }

    public void writeToolbarButton(JspWriter jspwriter, boolean flag, boolean isBootstrap)
            throws IOException {
        if (!isBootstrap) {
            jspwriter.print("<td");
            if (separator)
                jspwriter.print(" class=\"VSeparator\"");
            jspwriter.print(">");
        }
        write(jspwriter, Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(img),
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(disabledImg), label, labelPosition, href,
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> buttonClass).orElse(style), title, flag, isBootstrap);
        if (!isBootstrap)
            jspwriter.print("</td>");
    }

    public void writeWithoutRollover(JspWriter jspwriter, boolean flag, boolean isBootstrap)
            throws IOException {
        write(jspwriter, null, flag ? Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(img) :
                        Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> iconClass).orElse(disabledImg), false, label, labelPosition, flag ? href : null,
                Optional.of(isBootstrap).filter(x -> x.equals(Boolean.TRUE)).map(x -> buttonClass).orElse(style), title, null, accessKey, isBootstrap);
    }

    public Button clone() {
        try {
            return (Button) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}