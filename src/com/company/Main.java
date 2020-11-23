package com.company;

/*
Parser rules:
progr(expr, EOF)
expr(for | if_struct | more_less | plus_minus | assign)
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
        String input = "for (aaa < 123) do a + b";
        List<Lexeme> lexemes = lexAnalyze(input);
        System.out.print(lexemes);
        LexemBuffer lexemBuffer = new LexemBuffer(lexemes);
        programm(lexemBuffer);
    }

    public enum Lexeme_type{
        VARIABLE, EOF,
        OP_ASSIGN, /*OP_EQUAL*/ OP_MORE, OP_LESS, OP_PLUS, OP_MINUS, OP_MULT, OP_DIV,
        CYCLE_FOR, CYCLE_DO,
        CONDITIONAL_OPERATOR_IF, CONDITIONAL_OPERATOR_THEN, CONDITIONAL_OPERATOR_ELSE,
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
            return "Lexeme{"+ "type = "+ this.type + ", "+"value = \"" + this.value + '\"' + " }";
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
                /*case '=':
                    lexemes.add(new Lexeme(Lexeme_type.OP_EQUAL, c));
                    pos++;
                    continue;
                 */
                case '<':
                    lexemes.add(new Lexeme(Lexeme_type.OP_LESS, c));
                    pos++;
                    continue;
                case '>':
                    lexemes.add(new Lexeme(Lexeme_type.OP_MORE, c));
                    pos++;
                    continue;
                case ';':
                    lexemes.add(new Lexeme(Lexeme_type.SEMICOLON, c));
                    pos++;
                    continue;
                case ':':
                    if (expText.charAt(pos+1) == '='){
                        pos++;
                        lexemes.add(new Lexeme(Lexeme_type.OP_ASSIGN, ":="));
                        pos++;
                        continue;
                    }
                    else {
                        throw new RuntimeException("Unexpected character after ':'!");
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
                        boolean in_res = false;
                        String sb_ = sb.toString();
                        for(Reserved val: Reserved.values()) {
                            String v = val.toString().toLowerCase();
                            if (sb_.equals(v)) {
                                in_res = true;
                                break;
                            }
                        }
                        if(in_res){
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
                            break;
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
            }
        }
        lexemes.add(new Lexeme(Lexeme_type.EOF, ""));
        return lexemes;
    }
    /*
Parser rules:
programm(expr, EOF)
    expr(plus_minus | assign);

        for(factor, condition, factor, do);
            do(plus_minus);
        if_struct(condition, then);
            then(plus_minus | plus_minus, then);
            else(plus_minus | assign);

                condition(equal | more_less);
                    equal(plus_minus (('=') plus_minus));
                    more_less(plus_minus(('<' | '>')plus_minus));

                        assign(factor (((':=') factor)factor));
                        plus_minus: (mult_div(('+' | '-') mult_div));
                            mult_div: factor(('*' | '/') factor);
                                   factor: NUMBER | '(' condition | plusminus ')' | variable | type | semicolon;
*/
    public static void programm(LexemBuffer lexemes){
        Lexeme lexeme = lexemes.next();
        StringBuilder sb = new StringBuilder();
        do {
            switch (lexeme.type) {
                case CYCLE_FOR -> sb.append(for_cycle(lexemes));
                case CONDITIONAL_OPERATOR_IF -> sb.append(if_op(lexemes));
                case NUMBER -> sb.append(plus_minus(lexemes));
                case EOF -> sb.append(eof(lexemes));
            }
            lexeme = lexemes.next();
        } while (lexeme.type != Lexeme_type.EOF);
        System.out.print(sb.toString());
    }
    public static String eof(LexemBuffer lexemes){
        return "End of programm";
    }
    public static String for_cycle(LexemBuffer lexemes){
        return "for cycle with condtion: " + condition_do(lexemes);
    }
    public static String do_cycle(LexemBuffer lexemes){
        return "do cycle with body: " + plus_minus(lexemes);
    }
    public static String if_op(LexemBuffer lexemes){
        return "if with cindition: " + condition_if(lexemes);
    }
    public static String then_op(LexemBuffer lexemes){
        return "THEN: " + plus_minus(lexemes) + else_op(lexemes);
    }
    public static String else_op(LexemBuffer lexemes){
        return "ELSE: " + more_less(lexemes);
    }
    public static String condition_if(LexemBuffer lexemes){
        Lexeme lexeme = lexemes.next();
        StringBuilder sb = new StringBuilder();
        if(lexeme.type == Lexeme_type.LEFT_BRACKET){
            sb.append(lexeme.value);
            sb.append(more_less(lexemes));
            lexeme = lexemes.next();
            if (lexeme.type == Lexeme_type.RIGHT_BRACKET){
              sb.append(lexeme.value);
            }
            else throw new RuntimeException("Expected ')' in position " + lexemes.pos);
        }
        else throw new RuntimeException("Expected '(' in position " + lexemes.pos);
        lexeme = lexemes.next();
        return sb.toString() + then_op(lexemes);
    }
    public static String condition_do(LexemBuffer lexemes){
        Lexeme lexeme = lexemes.next();
        StringBuilder sb = new StringBuilder();
        if(lexeme.type == Lexeme_type.LEFT_BRACKET){
            sb.append(lexeme.value);
            sb.append(more_less(lexemes));
            lexeme = lexemes.next();
            if (lexeme.type == Lexeme_type.RIGHT_BRACKET){
                sb.append(lexeme.value);
            }
            else throw new RuntimeException("Expected ')' in position " + lexemes.pos);
        }
        else throw new RuntimeException("Expected '(' in position " + lexemes.pos);
        lexeme = lexemes.next();
        return sb.toString() + do_cycle(lexemes);
    }
    public static String more_less(LexemBuffer lexemes){
        String first = plus_minus(lexemes);
        String second = "";
        String operator = "";
        if(!first.isEmpty()) {
            operator = "";
            Lexeme lexeme = lexemes.next();
            if (lexeme.type == Lexeme_type.OP_MORE || lexeme.type == Lexeme_type.OP_LESS) {
                operator = lexeme.value;
            }
            if (!operator.isEmpty()){
                second = plus_minus(lexemes);
            }
            else{
                lexemes.back();
            }
        }
        return first+operator+second;
    }
    public static String plus_minus(LexemBuffer lexemes){
        String first = mult_div(lexemes);
        String second = "";
        String operator = "";
        if(!first.isEmpty()) {
            operator = "";
            Lexeme lexeme = lexemes.next();
            if (lexeme.type == Lexeme_type.OP_PLUS || lexeme.type == Lexeme_type.OP_DIV) {
                operator = lexeme.value;
            }
            if (!operator.isEmpty()){
                second = mult_div(lexemes);
            }
            else{
                lexemes.back();
            }
        }
        return first+operator+second;
    }
    public static String mult_div(LexemBuffer lexemes){
            String first = factor(lexemes);
            String second = "";
            String operator = "";
            if(!first.isEmpty()) {
                operator = "";
                Lexeme lexeme = lexemes.next();
                if (lexeme.type == Lexeme_type.OP_DIV || lexeme.type == Lexeme_type.OP_MULT) {
                    operator = lexeme.value;
                }
                if (!operator.isEmpty()){
                    second = factor(lexemes);
                }
                else{
                    lexemes.back();
                }
            }
            return first+operator+second;
    }
    public static String factor(LexemBuffer lexemes){
        Lexeme lexeme = lexemes.next();
        return switch (lexeme.type) {
            case NUMBER -> "number: " + lexeme.value;
            case VARIABLE -> "variable: " + lexeme.value;
            case LEFT_BRACKET, RIGHT_BRACKET -> lexeme.value;
            case SEMICOLON -> ";";
            default -> throw new RuntimeException("Wrong token in position " + lexemes.pos);
        };
    }
}