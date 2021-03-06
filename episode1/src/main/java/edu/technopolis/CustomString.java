package edu.technopolis;

import java.io.Serializable;
import java.util.Objects;

/**
 * Реализованная специальным образом строка (аналог {@link java.lang.String}),
 * хранящий содержимое строки кусочками (chunks) для лучшего переиспользования памяти при активном
 * создании подстрок.
 */
public class CustomString implements CharSequence, Serializable{
    private int offset;              //индекс с которого начинается подстрока
    private int count;               //длина хранимой подстроки
    private char[][] data;           //строка, разбитая на кусочки
    private int chunkLength = 10;    //длина кусочков
    private int dataLength;          //общая длина строки

    public CustomString(String inputString) {
        Objects.requireNonNull(inputString);
        if(inputString.length() == 0){
            return;
        }
        offset = 0;
        count = 0;
        dataLength = inputString.length();
        if(inputString.length() % chunkLength == 0){
            data = new char[inputString.length()/chunkLength][chunkLength];
            for(int i = 0; i < inputString.length(); i++){
                data[i/chunkLength][i%chunkLength] = inputString.charAt(i);
            }
        }
        else{
            data = new char[inputString.length()/chunkLength+1][];
            for(int i = 0; i < inputString.length()/chunkLength; i++){
                data[i] = new char[chunkLength];
            }
            data[inputString.length()/chunkLength] = new char[inputString.length()%chunkLength];
            for(int i = 0; i < inputString.length(); i++){
                data[i/chunkLength][i%chunkLength] = inputString.charAt(i);
            }
        }
    }
    public CustomString(int offset, int count, char[][] data, int dataLength) {
        this.offset = offset;
        this.count = count;
        this.data = data;
        this.dataLength = dataLength;
    }

    @Override
    public int length() {
        return (count == 0) ? dataLength : count;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }
        return data[(offset+index)/chunkLength][(offset+index)%chunkLength];
    }

    @Override
    public CustomString subSequence(int start, int end) {
        if (start < 0 || start > end || end > length()) {
            throw new IndexOutOfBoundsException();
        }
        return new CustomString(offset + start, end - start, data, dataLength);
    }

    @Override
    public String toString() {
        StringBuilder outString = new StringBuilder();
        for(int i = 0; i < this.length(); i++){
            outString.append(data[(offset + i)/chunkLength][(offset + i)%chunkLength]);
        }
        return outString.toString();
    }

    public static void main(String[] args) {
        CustomString SomeString = new CustomString("I want to break free");
        CustomString SubString = SomeString.subSequence(2, 15);
        CustomString Word = SubString.subSequence(8, 13);
        System.out.println(SomeString.toString());
        System.out.println(SubString.toString());
        System.out.println(Word.toString());
        System.out.println(SomeString.data);
        System.out.println(SubString.data);
        System.out.println(Word.data);
    }
}