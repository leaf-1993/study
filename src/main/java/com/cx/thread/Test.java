package com.cx.thread;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenxiang
 * @create 2021-12-21 18:26
 */
public class Test {
    public static void main(String[] args) throws DocumentException {
        int a = Integer.SIZE - 3;
        System.out.println(Integer.toBinaryString(a));
        int capacity = (1 << a) ;
        System.out.println(Integer.toBinaryString(capacity));

//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><![CDATA[&]]></a>";
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<process-definition name=\"CGHT_903\">\n" +
                "    <description>\n" +
                "    </description>\n" +
                "    <start-state id=\"开始\" name=\"开始\">\n" +
                "        <transition  id=\"开始提交人_同意\" name=\"开始提交人_同意\" fromid=\"开始\" from=\"开始\" to=\"提交人\" toid=\"提交人\">\n" +
                "            <condition>#{pass}==1</condition>\n" +
                "        </transition>\n" +
                "    </start-state>\n" +
                "    <end-state id=\"结束\" name=\"结束\">\n" +
                "    </end-state>\n" +
                "    <task-node  id=\"提交人\" name=\"提交人\" >\n" +
                "        <transition  id=\"提交人项目经理审核_同意\" name=\"提交人项目经理审核_同意\" fromid=\"提交人\" from=\"提交人\" to=\"项目经理审核\" toid=\"项目经理审核\">\n" +
                "            <condition>#{pass==1}&amp;&amp; #{ht}< 300</condition>\n" +
                "        </transition>\n" +
                "        <event  type=\"node-enter\">\n" +
                "            \n" +
                "                <variable  access=\"read,required,write\" name=\"overtime\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"approvefactor\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"ccs\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"editable\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"activepooltype\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"acceptemail\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"codenotnull\"/>\n" +
                "                <expression>overtime=\"null\";approvefactor=\"null\";ccs=\"null\";activepooltype=\"role\";editable=\"null\";acceptemail=\"0\";codenotnull=\"null\"</expression>\n" +
                "            \n" +
                "        </event>\n" +
                "        <task>\n" +
                "            <assignment  pooled-actors=\"#{commituser}\"/>\n" +
                "        </task>\n" +
                "    </task-node>\n" +
                "    <task-node  id=\"项目经理审核\" name=\"项目经理审核\" >\n" +
                "        <transition  id=\"项目经理审核结束_同意\" name=\"项目经理审核结束_同意\" fromid=\"项目经理审核\" from=\"项目经理审核\" to=\"结束\" toid=\"结束\">\n" +
                "            <condition>#{pass==1}</condition>\n" +
                "        </transition>\n" +
                "        <event  type=\"node-enter\">\n" +
                "            \n" +
                "                <variable  access=\"read,required,write\" name=\"overtime\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"approvefactor\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"ccs\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"editable\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"activepooltype\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"acceptemail\"/>\n" +
                "                <variable  access=\"read,required,write\" name=\"codenotnull\"/>\n" +
                "                <expression>overtime=\"null\";approvefactor=\"null\";ccs=\"null\";activepooltype=\"role\";editable=\"null\";acceptemail=\"0\";codenotnull=\"null\"</expression>\n" +
                "            \n" +
                "        </event>\n" +
                "        <task>\n" +
                "            <assignment  pooled-actors=\"r1000000000\"/>\n" +
                "        </task>\n" +
                "    </task-node>\n" +
                "</process-definition>";
        Document document = DocumentHelper.parseText(handleXml(xml));
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        System.out.println(elements);
        System.out.println(document);
    }

    public static String handleXml(String s) {
        String regex = "<>.*?</condition>";
        final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher ma = pa.matcher(s);
        while (ma.find()) {
            String group = ma.group();
            String replace = "<condition><![CDATA[" + group.replace("<condition>", "").replace("</condition>", "") + "]]></condition>";
            s = s.replace(group, replace);
        }
        return s;
    }
}
