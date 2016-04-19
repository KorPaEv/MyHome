package com.korpaev.myhome;

public class Translit
{
    public static String RusToLat(char ch)
    {
        switch (ch)
        {
            case ' ': return " ";
            case 'а': return "a";
            case 'б': return "b";
            case 'в': return "v";
            case 'г': return "g";
            case 'д': return "d";
            case 'е': return "e";
            case 'ё': return "e";
            case 'ж': return "zh";
            case 'з': return "z";
            case 'и': return "i";
            case 'й': return "j";
            case 'к': return "k";
            case 'л': return "l";
            case 'м': return "m";
            case 'н': return "n";
            case 'о': return "o";
            case 'п': return "p";
            case 'р': return "r";
            case 'с': return "s";
            case 'т': return "t";
            case 'у': return "u";
            case 'ф': return "f";
            case 'х': return "kh";
            case 'ц': return "ts";
            case 'ч': return "ch";
            case 'ш': return "sh";
            case 'щ': return "sch";
            case 'ъ': return "'";
            case 'ы': return "i";
            case 'ь': return "'";
            case 'э': return "eh";
            case 'ю': return "u";
            case 'я': return "ya";
            case 'А': return "A";
            case 'Б': return "B";
            case 'В': return "V";
            case 'Г': return "G";
            case 'Д': return "D";
            case 'Е': return "E";
            case 'Ё': return "E";
            case 'Ж': return "ZH";
            case 'З': return "Z";
            case 'И': return "I";
            case 'Й': return "J";
            case 'К': return "K";
            case 'Л': return "L";
            case 'М': return "M";
            case 'Н': return "N";
            case 'О': return "O";
            case 'П': return "P";
            case 'Р': return "R";
            case 'С': return "S";
            case 'Т': return "T";
            case 'У': return "U";
            case 'Ф': return "F";
            case 'Х': return "KH";
            case 'Ц': return "TS";
            case 'Ч': return "CH";
            case 'Ш': return "SH";
            case 'Щ': return "SCH";
            case 'Ъ': return "'";
            case 'Ы': return "I";
            case 'Ь': return "'";
            case 'Э': return "EH";
            case 'Ю': return "U";
            case 'Я': return "YA";

            case 'a': return "a";
            case 'b': return "b";
            case 'c': return "c";
            case 'd': return "d";
            case 'e': return "e";
            case 'f': return "f";
            case 'g': return "g";
            case 'h': return "h";
            case 'i': return "i";
            case 'j': return "j";
            case 'k': return "k";
            case 'l': return "l";
            case 'm': return "m";
            case 'n': return "n";
            case 'o': return "o";
            case 'p': return "p";
            case 'q': return "q";
            case 'r': return "r";
            case 's': return "s";
            case 't': return "t";
            case 'u': return "u";
            case 'v': return "v";
            case 'w': return "w";
            case 'x': return "x";
            case 'y': return "y";
            case 'z': return "z";
            case 'A': return "A";
            case 'B': return "B";
            case 'C': return "C";
            case 'D': return "D";
            case 'E': return "E";
            case 'F': return "F";
            case 'G': return "G";
            case 'H': return "H";
            case 'I': return "I";
            case 'J': return "J";
            case 'K': return "K";
            case 'L': return "L";
            case 'M': return "M";
            case 'N': return "N";
            case 'O': return "O";
            case 'P': return "P";
            case 'Q': return "Q";
            case 'R': return "R";
            case 'S': return "S";
            case 'T': return "T";
            case 'U': return "U";
            case 'V': return "V";
            case 'W': return "W";
            case 'X': return "X";
            case 'Y': return "Y";
            case 'Z': return "Z";

            default: return String.valueOf(ch);
        }
    }

    public static String RusToLat(String s)
    {
        StringBuilder sb = new StringBuilder(s.length()*2);
        for(char ch: s.toCharArray())
        {
            sb.append(RusToLat(ch));
        }
        return sb.toString();
    }
}
