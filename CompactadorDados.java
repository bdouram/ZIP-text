
//Author: Bruno Dourado Miranda


package compactadordados;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;
import javax.swing.JOptionPane;

class NoLista {
    //Lista que irá receber as árvores e os símbolos presentes no txt;
    int code;
    NoLista prox;
    String letra;
}

class Vetor {
    //Vetor que irá receber as árvores e os símbolos presentes no txt;
    int code;
    String letra;
    NoArvore a;
}

class NoArvore  {
    //Lista que será usada para armazenar os símbolos encontrados no txt, e inserir em um vetor da classe Vetor;
    int code = 0;
    String letra;
    NoArvore filhoD = null;
    NoArvore filhoE = null;
}


class Ascii {
    
    public String leituraDeTxtOriginal() {
        String str = "";

        try (Scanner in = new Scanner(new File("C:\file.txt"))) {
            while (in.hasNextLine()) {
                str = str + in.nextLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Falha ao ler arquivo!");

        }

        return str;
    }
 
    public String leituraDeTxtModificado() {
        String str = "";
        try (Scanner in = new Scanner(new File("C:\file.txt"), "UTF-8")) {
            while (in.hasNextLine()) {
                str = str + in.nextLine();
                if (in.hasNextLine() != false) {
                    str = str + "\n";
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Falha ao ler o arquivo .txt");
        }

        if (str.indexOf("\n") > -1) {
            System.out.println("Quebra de linha detectada!");
        }

        return str;
    }
    
    public void escreveTxtCompactado(String str) {
        BufferedWriter novoArquivo;
        try {
            String newLine = "";
            novoArquivo = new BufferedWriter(new FileWriter("C:\file.txt"));
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) == '\n') {
                    novoArquivo.newLine();
                } else {
                    newLine = leituraDeTxtModificado();
                    novoArquivo.write(newLine + str.charAt(j));
                }

            }
            novoArquivo.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao criar o arquivo compactado!");
        }
    }

 

    public int retornaCodigo(String s) {
        int x = (int) s.charAt(0);//Nesse método,retorna-se o código Ascii de cada string que for
        return x; //enviada para o referido método;
    }

    public NoLista montaAscii(String t) {
        //Método que faz a varredura da String encontrada e retorna uma lista com os símbolos presentes no txt;
        NoLista l = new NoLista();
        String x = String.valueOf(t.charAt(0));
        l.code = 1;
        l.letra = x;

        for (int i = 1; i < t.length(); i++) {
            x = String.valueOf(t.charAt(i));
            insere(l, x, 1);
        }

        return l;
    }

    public NoLista insere(NoLista l, String s, int code) {
        //Lista que verifica se existe o símbolo encontrado no txt, se não houver, ele cria uma nova posição. Já se houver, ele acrescenta +1 na frequência; 
        NoLista aux = l;
        while (aux != null && !aux.letra.equals(s)) {
            aux = aux.prox;
        }
        if (aux == null) {
            aux = l;
            while (l.prox != null) {
                l = l.prox;
            }
            l.prox = new NoLista();
            l.prox.code = 1;
            l.prox.letra = s;
            l = aux;
        } else {
            aux.code = aux.code + 1;
        }
        return l;

    }

    public Vetor[] montaVetor(String t) {
        //Monta um vetor que possui o símbolo e o código do mesmo;
        NoLista l = montaAscii(t);
        NoLista aux = l;
        int i = 0;
        while (aux != null) {
            i++;
            aux = aux.prox;
        }

        Vetor nV[] = new Vetor[i];
        i = 0;

        while (l != null) {
            nV[i] = new Vetor();
            nV[i].code = l.code;
            nV[i].letra = l.letra;
            i++;
            l = l.prox;
        }
        return nV;
    }
    
    public int verificaByte(String bin){
        int codigo=(int) bin.charAt(0);        
        return codigo;
    }
    
    public String retornaStringOriginal(String str){
        //Método que remonta os códigos formados à partir dos símbolos inseridos no arquivo compactado;
        String t="";
        String bin="";
        int ascii=0;
        
        for(int i=1;i<str.length();i++){
            t=Character.toString(str.charAt(i));
            ascii=retornaCodigo(t);
            bin=bin+retornaSeMenorOitoBits(Integer.toString(ascii,2));
        }
        return bin;
    }
    
    public String remontaStringOriginal(String str, VetorBinario v[]) {
      //Método que realiza a conversão dos binários presentes no arquivo compactado para os símbolos originais;
        int byteDeControle = verificaByte(str);//Retorna os "0"'s inseridos para a formação de 1 byte;
        String novaString = retornaStringOriginal(str);
        String g = "", original = "";
        
        for (int i = 0; i < novaString.length(); i++) {
            if (i + byteDeControle < novaString.length()) {
                g = g + Character.toString(novaString.charAt(i));
                if (retornaLetra(g, v) != null) {
                    original = original + retornaLetra(g, v);
                    g = "";
                }

            }

        }
        return original;//retorna o texto escrito originalmente;
    }
    
    public int byteControle(String byteControle){
        int codigo=Integer.parseInt(byteControle,2);
        return codigo;
    }
    
    public String retornaLetra(String binario,VetorBinario v[]){
       //Método que retorna o símbolo se o código informado existir;  
        int i=0;
        while(i<v.length && !v[i].binario.equals(binario)){
            i++;
        }
        if(i!=v.length){
            return v[i].letra;
        }else{
            return null;
        }
    }
    
    public String retornaSeMenorOitoBits(String retornaBinarioOriginal) {
        //Método para a descompactação, se não houver 8 bits(1 byte) na string, ele insere zeros à esquerda, para formar 1 byte;
        if(retornaBinarioOriginal.length()==8){
            return retornaBinarioOriginal;
        }
        
        while (retornaBinarioOriginal.length() < 8) {
            retornaBinarioOriginal = "0" + retornaBinarioOriginal;
        }

        return retornaBinarioOriginal;
    }
   

}

class montaArvoreCompactada {

    public NoArvore montagemInicial(Vetor v[]) {
        //O método consiste em verificar a "grandeza escalar" das árvores e juntar as menores até formar 1 árvore só
        //prensente com todos os símbolos utilizados no txt original;
        int i = 0, j = 0, armazenaMenor = 0;

        while (verificaSeApenasDoisNulo(v)) {
            v = OrganizaVetorMenor(v);//O vetor é atualizado(as árvores são organizadas de maneira crescente)
            i = retornaMenor(v);//i retorna o índice onde a menor árvore está presente no vetor;
            armazenaMenor = v[i].code;//A frequência da árvore é armazenada
            v[i].code = -1;//e atribuída -1 ao seu código, simbolizando que a árvore já foi conferida;
            v = OrganizaVetorMenor(v);//O vetor é novamente reorganizado (em ordem crescente)
            j = retornaMenor(v);//e a segunda menor árvore é encontrada;

            if (v[i].a == null && v[j].a == null) {//Se as posições do vetor estão nulas,significa que nenhuma árvore foi criada com os dois símbolos;
                v[i].a = new NoArvore();//Cria-se uma nova árvore juntando as frequências dos dois símbolos
                v[i].a.code = armazenaMenor + v[j].code;
                v[i].a.filhoE = new NoArvore();
                v[i].a.filhoE.letra = v[i].letra;
                v[i].a.filhoD = new NoArvore();
                v[i].a.filhoD.letra = v[j].letra;
                v[i].code = armazenaMenor + v[j].code;
                v[j].a = null;//Mesmo a posição ainda alocada, "anula-se" a árvore inserida numa das posições
                v[j].letra = null;
                v[j].code = -1;//Atribuíndo -1 (Árvore conferida);
            } else {
                //Faz-se o mesmo para cada posição, verificando se existem árvores alocadas, evitando perdas de símbolos;
                if (v[i].a != null && v[j].a == null) {
                    v[j].a = new NoArvore();
                    v[j].code = armazenaMenor + v[j].code;
                    v[j].a.code = v[j].code;
                    v[j].a.filhoE = new NoArvore();
                    v[j].a.filhoE.letra = v[j].letra;
                    v[j].a.filhoD = v[i].a;
                    v[i].a = null;
                    v[i].letra = null;
                    v[i].code = -1;
                } else {
                    if (v[i].a != null && v[j].a != null) {
                        NoArvore t = new NoArvore();
                        t.code = armazenaMenor + v[j].code;
                        t.filhoD = v[i].a;
                        t.filhoE = v[j].a;
                        v[i].a = t;
                        v[i].code = t.code;
                        v[j].a = null;
                        v[j].letra = null;
                        v[j].code = -1;
                    } else {
                        if (v[i].a == null && v[j].a != null) {
                            v[i].a = new NoArvore();
                            v[i].a.code = armazenaMenor + v[j].code;
                            v[i].a.filhoE = new NoArvore();
                            v[i].a.filhoE.letra = v[i].letra;
                            v[i].a.filhoD = v[j].a;
                            v[i].code = armazenaMenor + v[j].code;
                            v[j].a = null;
                            v[j].letra = null;
                            v[j].code = -1;

                        }
                    }
                }
            }
        }
        return entregaVetorPronto(v);//método que retorna o nó da árvore;
    }

    public int retornaMenor(Vetor v[]) {
        //Método que retorna o índice do vetor do símbolo que é o menor de todos; 
        int menor = 0;
        while (menor < v.length && v[menor].code == -1) {
            menor++;
        }

        return menor;
    }

    public Vetor[] OrganizaVetorMenor(Vetor v[]) {
        //Organiza o vetor onde as árvores e os símbolos do arquivo original estão presentes em ordem crescente
        int sMenor = 0;
        Vetor trocaDePosicao = null;
        while (sMenor < v.length - 1) {
            if (v[sMenor] != null && v[sMenor + 1] != null && v[sMenor].code > v[sMenor + 1].code) {
                trocaDePosicao = v[sMenor];
                v[sMenor] = v[sMenor + 1];
                v[sMenor + 1] = trocaDePosicao;
                sMenor = 0;
            }
            sMenor++;
        }

        return v;
    }

    public NoArvore entregaVetorPronto(Vetor v[]) {
        //O método acima une as duas últimas árvores de Huffman presentes no vetor, e retorna o nó cabeça de lista da árvore
        if (v.length > 1) {

            int i = 0;
            while (v[i].code == -1) {
                i++;
            }
            int j = i + 1;

            while (v[j].code == -1) {
                j++;
            }

            NoArvore t = new NoArvore();
            t.code = v[i].code + v[j].code;

            if (v[i].a != null && v[j].a == null) {
                v[j].a = new NoArvore();
                v[j].a.letra = v[j].letra;
                t.filhoD = v[i].a;
                t.filhoE = v[j].a;
            }
            if (v[i].a == null && v[j].a != null) {
                v[i].a = new NoArvore();
                v[i].a.letra = v[i].letra;
                t.filhoD = v[i].a;
                t.filhoE = v[j].a;
            }
            if (v[i].a == null && v[j].a == null) {
                t.filhoD = new NoArvore();
                t.filhoE = new NoArvore();
                t.filhoD.letra = v[i].letra;
                t.filhoE.letra = v[j].letra;
            }
            
            if(v[i].a!=null && v[j].a!=null){
                t.filhoD = v[i].a;
                t.filhoE = v[j].a;
            }

            v[i].letra = null;
            v[i].code = t.code;
            v[j].a = null;
            v[j].letra = null;
            v[j].code = -1;

            return t;

        } else {
            NoArvore t = new NoArvore();
            t.code = v[0].code;
            t.letra = v[0].letra;
            return t;
        }
        
       
    }

    public boolean verificaSeApenasDoisNulo(Vetor v[]) {
        //O método verifica se há apenas duas árvores no vetor(apenas condição de parada);
        int i = 0, count = 0;
        while (i < v.length) {
            if (v[i].code != -1) {
                count++;
            }
            i++;
        }
        return count > 2;
    }

}

class VetorBinario implements Serializable {
    //Classe criada para armazenar o símbolo, o binário e o decimal de cada símbolo inserido;
    String letra;
    String binario;
    int decimal;

}

class DefineBinario {

    public void montaBin(NoArvore no, String monta, VetorBinario f[]) {
       //Método que percorre a árvore e identifica o código de cada símbolo inserido no txt;   
        if (no != null) {
            if (no.filhoE == null && no.filhoD == null) {
                montaVetorBinario(f, monta, no.letra);
            } else {
                montaBin(no.filhoE, monta + "0", f);
                montaBin(no.filhoD, monta + "1", f);
            }
        }
    }

    public VetorBinario[] montaVetorBinario(VetorBinario v[], String binMontado, String letra) {
        //O método que varre o vetor de binários, encontra a 1ª posição nula mais próxima e aloca a letra encontrada;
        int i = 0;
        while (v[i] != null && i < v.length) {
            i++;
        }
        
        if(binMontado.equals("")) binMontado="0";
        v[i] = new VetorBinario();
        v[i].letra = letra;
        v[i].binario = binMontado;
        v[i].decimal = Integer.parseInt(binMontado, 2);
        return v;//O vetor que será retornado aloca símbolos diferentes uns dos outros que são encontrados no txt original;
    }

    
}

class MontaNovaString {

    public int encontraCadaLetra(VetorBinario v[], String l) {
        int i = 0;
        while (!v[i].letra.equals(l)) {
            i++;
        }
        return i;//retorna o índice do vetor em que o simbolo encontrado está presente;
    }

    public String montaStringBinaria(VetorBinario v[], String original) {
        //Método que retorna a numeração binária de cada símbolo presente no txt;
        String estranha = "";
        for (int i = 0; i < original.length(); i++) {
            estranha = estranha + v[encontraCadaLetra(v, Character.toString(original.charAt(i)))].binario;
        }
        return estranha;
    }
    
    public String montaLetra(String t) {
        int contador = 0, i = 0;//t é a String com os binários presentes nos códigos da Arvore de Huffman
        String m = "", montaS = "";//String que será montada, para ser anexada no novo arquivo txt a ser criado;
        String byteControle = "";//byteControle é uma variável que irá ser transformada em um novo byte inserido no começo do texto que tem a função de retornar a quantidade de "0"'s inseridos ao fim do texto;
        int contaBits = 0;
 
        while (i < t.length()) {
            contador = 0;
            m = "";
            while (i < t.length() && contador < 8) {//Montando byte a byte um novo símbolo
                m = m + Character.toString(t.charAt(i));
                contador++;
                i++;
            }

            while (contador < 8) {//Se não houver bits para formar um byte, zeros à direita são inseridos até formar um byte completo
                m = m + "0";
                contador++;
                contaBits++;//contaBits servirá para contar a quantidade de zeros inseridos à direita.
            }               //contaBits será um símbolo inserido no início de cada texto;
            
            
            int codigo = Integer.parseInt(m, 2);//O código presente na numeração binária do byte é convertido
            char letra = (char) codigo;//Em um símbolo qualquer, para ser inserido no texto compactado pelo código;
            
            
            montaS = montaS + Character.toString(letra);//E os símbolos vão sendo somados até formar um texto com símbolos
        }

        byteControle = Integer.toString(contaBits, 2);//Convertendo contaBits em 1 byte;
        int codigo = Integer.parseInt(byteControle, 2);//Utilizando o código ascii do byte
        char bControle = (char) codigo;//e transformando o byte num símbolo
        return Character.toString(bControle) + montaS;//Que será inserido no início do arquivo compactado;
    }
    
}

class JogaArvoreTxt {

    JogaArvoreTxt(VetorBinario v[]) {
        try {//Inserindo o vetor de binários em um arquivo txt, para posteriormente ser lido;
            ObjectOutputStream arch = new ObjectOutputStream(new FileOutputStream("C:\newFile.txt"));
            arch.writeObject(v);
            arch.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Falha na construção de arquivo para armazenagem de dados!");
        }

    }

}

class LeArvore {

    public VetorBinario[] LeituraArvore() throws ClassNotFoundException {
        try {//Retornando o vetor de binários presentes no arquivo texto;
            FileInputStream leituraTxt = new FileInputStream("C:\newFile.txt");
            ObjectInputStream A = new ObjectInputStream(leituraTxt);
            VetorBinario vetor[] = (VetorBinario[]) A.readObject();
            leituraTxt.close();
            return vetor;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Falha na leitura da compactação!");
        }
        return null;
    }

    public void escreveTxtDescompactado(String str) {
        FileWriter novoArquivo;
        try {//Inserindo a string lida no arquivo compactado para um novo txt;
            novoArquivo = new FileWriter(new File("C:\output.txt"));
            novoArquivo.write(str);
            novoArquivo.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao criar o arquivo Descompactado!");
        }
    }

}

public class CompactadorDados {

    public static void main(String[] args) throws ClassNotFoundException {
        //O trabalho foi elaborado da seguinte forma: Uma string é capturada a partir de um arquivo texto,
        //Foi calculada a quantidade de frêquencias que um determinado símbolo aparece no txt.
        //Montou-se uma árvore de Huffman, presente em um vetor da classe Vetor, retornando assim o nó de uma árvore(classe Nó Arvore)
        //Com o nó dessa árvore, monta-se um vetor (1 string para os binários e 1 string para os símbolos)com cada símbolo presente no texto,
        //Armazena-se esse vetor em um txt para a descompactação. 
               
        Ascii obj = new Ascii();
        
        JOptionPane.showMessageDialog(null,"\tAviso!\n"+"O compactador não reconhece caracteres da língua portuguesa(ç ~ ^)devido ao Windows!");
        String leituraTextoOriginal=obj.leituraDeTxtOriginal();
        String leituraTextoModificado="";
        
        if (leituraTextoOriginal != "") {

            Vetor nV[] = obj.montaVetor(leituraTextoOriginal);// Método que monta o vetor que contém as strings para a árvore; 
            montaArvoreCompactada trie = new montaArvoreCompactada();
            NoArvore arvore = new NoArvore();
            arvore = trie.montagemInicial(nV);//Um objeto armazena a árvore, para a inserção em um vetor que contém o símbolo e o código binário correspondente;

            VetorBinario vetorBinario[] = new VetorBinario[nV.length];
            DefineBinario objD = new DefineBinario();
            objD.montaBin(arvore, "", vetorBinario);
            JogaArvoreTxt objArvore = new JogaArvoreTxt(vetorBinario);//Inserindo o vetor de binários e seus símbolos em um txt;
            nV = null;

            MontaNovaString insereNoTxt = new MontaNovaString();
            String strBinModificada = insereNoTxt.montaStringBinaria(vetorBinario, leituraTextoOriginal);//Recebe a string que será convertida nos caracteres para a compactação
            String strAInserir = insereNoTxt.montaLetra(strBinModificada);

            obj.escreveTxtCompactado(strAInserir);//Método que escreve os caracteres compactados em um novo txt;

            vetorBinario = null;//Desvinculando compactador e descompactador;
            arvore = null;
            VetorBinario v2[] = null;//Criando um novo vetor que receberá o txt de vetorBinario
            LeArvore novaArvore = new LeArvore();
            v2 = novaArvore.LeituraArvore();

            leituraTextoModificado = obj.leituraDeTxtModificado();//Leitura do txt compactado
            strAInserir = obj.remontaStringOriginal(leituraTextoModificado, v2);//retornando a String presente no txt compactado;
            novaArvore.escreveTxtDescompactado(strAInserir);
            JOptionPane.showMessageDialog(null,"Texto compactado e descompactado com sucesso!\n"+"Confira a pasta do arquivo fonte!");

        }else{
            JOptionPane.showMessageDialog(null,"Nenhum texto inserido no arquivo para compactação!");
           
        }
        
    }

}
//Fim_algoritmo