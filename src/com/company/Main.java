package com.company;

/*
Parser rules:
assign(factor (((':=') factor)factor));
for(condition, do);
do(plus_minus);
if_struct(condition, then);
then(plus_minus | plus_minus, then);
condition(equal | more_less);
equal(plus_minus (('=') plus_minus));
more_less(plus_minus(('<' | '>')plus_minus));
else(plus_minus);
plus_minus: (mult_div(('+' | '-') mult_div));
mult_div: factor(('*' | '/') factor);
factor: NUMBER | '(' expr ')' | variable | type | semicolon;



*/


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

    }

    public enum Lexeme_type{
        VARIABLE,
        OP_ASSIGN, OP_EQUAL, OP_MORE, OP_LESS, OP_PLUS, OP_MINUS, OP_MULT, OP_DIV,
        CYCLE_FOR, CYCLE_DO,
        CONDITIONAL_OPERATOR_IF, CONDITIONAL_OPERATOR_THEN, CONDITIONAL_OPERATOR_ELSE,
        CONDITION,
        TYPE, NUMBER, LEFT_BRACKET, RIGHT_BRACKET, VAR, SEMICOLON}
    public enum Letters{a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z}
    public enum Reserved{FOR, DO, IF, THEN, ELSE}
    public static class Lexeme{
        Lexeme_type type;
        String value;
        public Lexeme(Lexeme_type type, String value){
            this.type = type;
            this.value = value;
        }
        public Lexeme(Lexeme_type type, Character value){
            this.type = type;
            this.value = value.toString();
        }
        @Override
        public String toString(){
            return "Lexeme{"+ "type = "+ this.type + ", "+"value = " + this.value + '\'' + " }";
        }
    }

    public static class LexemBuffer{
        private int pos;
        public List<Lexeme> lexemes;

        public LexemBuffer(List<Lexeme> lexemes){
            this.lexemes = lexemes;
        }
        public Lexeme next(){
            return lexemes.get(pos++);
        }
        public void back(){
            pos--;
        }
        public int getPos(){
            return pos;
        }
    }

    public static List<Lexeme> lexAnalyze (String expText){
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int pos = 0;
        while(pos < expText.length()){
            char c = expText.charAt(pos);
            switch (c){
                case ' ':
                    pos++;
                    continue;
                case '(':
                    lexemes.add(new Lexeme(Lexeme_type.LEFT_BRACKET, c));
                    pos++;
                    continue;
                case ')':
                    lexemes.add(new Lexeme(Lexeme_type.RIGHT_BRACKET, c));
                    pos++;
                    continue;
                case '+':
                    lexemes.add(new Lexeme(Lexeme_type.OP_PLUS, c));
                    pos++;
                    continue;
                case '-':
                    lexemes.add(new Lexeme(Lexeme_type.OP_MINUS,c));
                    pos++;
                    continue;
                case '*':
                    lexemes.add(new Lexeme(Lexeme_type.OP_MULT, c));
                    pos++;
                    continue;
                case '/':
                    lexemes.add(new Lexeme(Lexeme_type.OP_DIV, c));
                    pos++;
                    continue;
                case '=':
                    lexemes.add(new Lexeme(Lexeme_type.OP_EQUAL, c));
                    pos++;
                    continue;
                case ':':
                    if (expText.charAt(pos++) == '='){
                        lexemes.add(new Lexeme(Lexeme_type.OP_ASSIGN, ":="));
                        pos++;
                        continue;
                    }
                    else {
                        throw new RuntimeException("Unexpected character after ':'");
                    }

                default:
                    if (Character.isLetter(c)){
                        StringBuilder sb = new StringBuilder();
                        do{
                            sb.append(c);
                            pos++;
                            if(pos>=expText.length()){
                                break;
                            }
                            c = expText.charAt(pos);
                        } while (Character.isLetter(c) || Character.isDigit(c) || c == '_');

                        for(Reserved val: Reserved.values()){
                            String v = val.toString().toLowerCase();
                            String sb_ = sb.toString();
                            if(sb_.equals(v)){
                                switch (sb_) {
                                    case "for" -> {
                                        lexemes.add(new Lexeme(Lexeme_type.CYCLE_FOR, sb_));
                                        pos++;
                                    }
                                    case "do" -> {
                                        lexemes.add(new Lexeme(Lexeme_type.CYCLE_DO, sb_));
                                        pos++;
                                    }
                                    case "if" -> {
                                        lexemes.add(new Lexeme(Lexeme_type.CONDITIONAL_OPERATOR_IF, sb_));
                                        pos++;
                                    }
                                    case "then" -> {
                                        lexemes.add(new Lexeme(Lexeme_type.CONDITIONAL_OPERATOR_THEN, sb_));
                                        pos++;
                                    }
                                    case "else" -> {
                                        lexemes.add(new Lexeme(Lexeme_type.CONDITIONAL_OPERATOR_ELSE, sb_));
                                        pos++;
                                    }
                                    default -> {
                                        lexemes.add(new Lexeme(Lexeme_type.VARIABLE, sb_));
                                        pos++;
                                    }
                                }
                            }
                            else{
                                lexemes.add(new Lexeme(Lexeme_type.VARIABLE, sb_));
                                pos++;
                            }
                        }
                    }
                    else if(Character.isDigit(c)){
                        StringBuilder sb = new StringBuilder();
                        do{
                            sb.append(c);
                            pos++;
                            if(pos >= expText.length()){
                                break;
                            }
                            c = expText.charAt(pos);
                        }while(Character.isDigit(c));
                        lexemes.add(new Lexeme(Lexeme_type.NUMBER, sb.toString()));

                    }
                    else if(c != '_'){
                        throw new RuntimeException("Unexpected character: " + c);
                    }
//                    if (Character.isUpperCase(c)){
//                        StringBuilder sb = new StringBuilder();
//                        do{
//                            sb.append(c);
//                            pos++;
//                            if(pos >= expText.length()){
//                                break;
//                            }
//                            c=expText.charAt(pos);
//                        }while(Character.isLowerCase(c) || Character.isUpperCase(c) || Character.isDigit(c));
//                    }

            }
        }
        return lexemes;
    }

}



