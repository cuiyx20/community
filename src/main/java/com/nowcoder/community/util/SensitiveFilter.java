package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //提供对敏感词的替换成什么字符
    private static final String REPLACEMENT = "***";

    /*
    要做三部分的工作：
        1、定义这个前缀树
        2、使用敏感词建树，初始化
        3、使用前缀树过滤敏感词
     */

    //初始化前缀树
    //首先初始化一个根节点【什么都不放】
    private TrieNode rootNode = new TrieNode();

    @PostConstruct     //这个注解表示这是一个初始化方法，当容器实例化这个Bean【服务启动的时候】之后，在调用构造器之后，这个初始化方法会被自动的调用
    public void init(){
        try(
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt"); //加载到的是一个字节流，需要关闭【正常是在finally中关闭，但是java7给了一个语法就是可以在try中关闭】
            //直接使用这个字节流读文件不方便——可以将字节流转换成字符流new InputStreamReader(is);直接使用这个字符流可能也不太方便，我们最好使用一个缓冲流，效率更高
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));   //也在try中关掉
        ){
            //通过reader读取敏感词
            String keyword;
            while((keyword = reader.readLine()) != null){
                //将一个敏感词添加到前缀树中
                this.addKeyword(keyword);
            }
        }catch (IOException e){
            logger.error("加载敏感词文件失败：" + e.getMessage());
        }
    }

    //将一个敏感词添加到前缀树中去
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;  //指针，默认指向根节点
        for(int i = 0;i < keyword.length();i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            //指向子节点，进入下一轮循环
            tempNode = subNode;

            //最后一个字符打标记
            if(i == keyword.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }


    //实现过滤使用前缀树
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }

        //定义三个指针
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        StringBuilder sb = new StringBuilder();
        while(position < text.length()){     //使用指针3的效率更高一点，少遍历几次
            char c = text.charAt(position);

            //跳过符号——有些人很机智在敏感词中间加上符号来避免被识别
            if(isSymbol(c)){
                //若指针1位于根节点，就把它计入结果，然后指针2向后移动
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                position++;    //无论是否遇到特殊符号，指针3都++
                continue;
            }
            //c不是特殊符号，就要检查下级节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                //以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                begin++;
                position = begin;
                //指针1重新指向根节点
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                //发现敏感词，将begin_position之间的敏感词替换成
                sb.append(REPLACEMENT);
                position++;
                begin = position;
            }else{
                //检测途中没检测完
                position++;
            }
        }
        //将position到终点而begin还没到的中间的字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }
    //判断是否是符号
    private boolean  isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);    //东亚的文字范围
    }
    private class TrieNode{
        //定义前缀树的数据结构

        //关键词结束的标识
        private boolean isKeywordEnd = false;

        //子节点(key是下级字符，value是下级节点)
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //提供添加子节点的方法——公有的方法
        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c,node);
        }

        //提供一个获取子节点的方法——公有
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
