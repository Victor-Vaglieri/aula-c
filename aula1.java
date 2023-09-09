import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

class sufAndInf {

    private String texto; // string que fai ser usada
    private String sufix; //local para guardar a expressão sufixa


    // metodo publico
    public sufAndInf(String texto){
        this.texto = texto;
    }

    // metodo publico quando recebe "vazio"
    public sufAndInf(){
        this("");
    }

    // set
    public void setTexto(String texto){
        this.texto = texto;
    }

    // get
    public String getTexto(){
        return texto;
    }
    
    // verifica se fecha os parenteses
    public boolean verifica(){
        char[] pilha = new char[texto.length()]; // cria a pilha
        int peeko = -1; // topo da pilha

        for (int i = 0; i < texto.length(); i++) { // percorre o texto
            if (texto.charAt(i) == '(') { // caso for "abre" parenteses
                pilha[++peeko] = texto.charAt(i); // coloca na pilha
            } else if (texto.charAt(i) == ')') { // caso for "fecha" parenteses
                if (peeko == -1) { // caso não tiver nada na pilha retorna false
                    return false;
                } else { // caso tenha algo na pilha
                    if (texto.charAt(i) != ')') {
                        return false;
                    }
                }
            }
        }
        return peeko == -1;
    }


    //abaixo serve para converter infixo para sufixo

    
    // serve para retornar uma prioridade de operadores
    private int prioridade(char letra) {
        if ((letra == '+') || (letra == '-')){ return 1;}
        else if ((letra == '/') || (letra == '*')) { return 2;}
        else if (letra == '^') { return 3;}
        else {return -1;}
    }

    // metodo que transforma infixo para sufixo
    public String paSufix() {
        if (verifica() == true){

        
            String resultado = "";  // criando a variavel "resultado" 
            char[] pile = new char[texto.length()]; // pilha com muita aspas
            int peek = -1; // peeko da pilha
            for (int i = 0; i < texto.length(); i++) { // laço para o texto recebido
                char posicao = texto.charAt(i); // caractere com valor do laço
                if (Character.isLetter(posicao)) { // verifica se é letra
                    resultado += posicao+ " "; // se sim coloca na resultado
                } else if (posicao == '(') { // se for '(' coloca na pilha
                    pile[++peek] = posicao;
                } else if (posicao == ')') { // se for ')' da coloca no resultado o que estava na pilha ate achar o '('
                    while (peek >= 0 && pile[peek] != '(') {
                        resultado += pile[peek--] + " ";
                    }
                    if (peek < 0 || pile[peek] != '(') { // verifica se não é possivel tal façanha 
                        return "ERRO"; // caso sim retorna a mensagem "erro"
                    } else {
                    peek--;// caso não reduz o peeko 
                    }
                } else { // caso não ter mais nada
                    while (peek >= 0 && prioridade(posicao) <= prioridade(pile[peek])) { // coloca na resultado o que sobrou de acordo com a prioridade
                        resultado += pile[peek--] + " ";
                    }
                    pile[++peek] = posicao; // push no caractere 
                }
            }
            while (peek >= 0) {
                resultado += pile[peek--] + " "; // coloca na resultado o que sobrou
            }
            sufix = resultado; // guarda o resultado em sufix
            return resultado;

        } else {
            return "ERRO";
        }
    }


    //abaixo serve para converter sufixo para infixo

    // so serve para ver se o simbolo é uma operação
    public boolean verifOp(String cada) {
        return cada.equals("+") || cada.equals("-") || cada.equals("*") || cada.equals("/")|| cada.equals("^");
    }

    // metodo que transforma sufixo para infixo
    public String paInfix() {
        sufix = texto; // guarda o texto que veio para a variavel sufix
        String[] tudo = texto.split("\\s+");  //o texto dividido para a manipulação
        String[] pilha = new String[tudo.length]; //a pilha
        int peek = -1; // o peeko da pilha
        for (String cada : tudo) { // cada = cada elemento do texto dividido
            if (verifOp(cada)) { // achar uma operação 
                String letra2 = pilha[peek--]; // pega a ultima letra usando o pop
                String letra1 = pilha[peek--]; // pega a penultima letra usando o pop
                pilha[++peek] = "(" + letra1 + " " + cada + " " + letra2 + ")"; // coloca com push a sentença com parenteses 
            } else { // se não for operação 
                pilha[++peek] = cada; // coloca na pilha com push
            }
        }
        return pilha[peek]; // retorna a pilha com um elemento vulgo a resposta
    }


    //abaixo serve para fazer a conta


    // metodo que pega os operando, verifica qual o operador e retorna o resultado 
    private double fazConta(char sinal, double op1, double op2) {
        if (sinal == '+') {return op1 + op2;}
        else if (sinal == '-') {return op1 - op2;}
        else if (sinal == '*') {return op1 * op2;}
        else if (sinal == '^') {return Math.pow(op1, op2);}
        else {return op1 / op2;}
        }
    


    // calcula o resultado
    public double resultado(){
        Scanner scan = new Scanner(System.in); // começa o scan
        Map<Character, Double> valores = new HashMap<>(); // cria um dicionario definindo um valor para cada letra
        char[] letras = sufix.replaceAll("[^A-Za-z]", "").toCharArray(); // transforma sufix em um vetor somente de letras
        double[] pil = new double[letras.length]; // cria a pilha
        int peeko = -1; // peeko da pilha

        for (int i = 0; i < sufix.length(); i++) { // começa o laço do tamanho de sufix
            char letra = sufix.charAt(i); // o caractere analisado
            if (Character.isLetter(letra)) { // se for letra
                if (!valores.containsKey(letra)) { // se a letra não esta dentro do dicionario
                    System.out.print("Digite o valor de " + letra + ": "); 
                    valores.put(letra, scan.nextDouble()); // pede o valor e coloca no dicionario
                }
                pil[++peeko] = valores.get(letra); // coloca o valor da letra na pilha
            } else if (letra == '+' || letra == '-' || letra == '*' || letra == '/' || letra == '^') { // caso for simbolo
                if (peeko >= 1) { // caso exista operandos
                    double op2 = pil[peeko--]; // pega o ultimo operando
                    double op1 = pil[peeko--]; // pega o penultimo operando 
                    pil[++peeko] = fazConta(letra, op1, op2); //coloca na pilha o resultado 
                } else {// caso não exista operandos
                    System.out.println("Erro"); // printa erro e retorna "nada"
                    return Double.NaN;
                }
            }
        }
        return pil[peeko]; // retorna o resultado
    }
}



class Operacao{

  public boolean CheckLetra(char simbolo){ // Método que verifica se o caracter dado é uma letra
    String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    if (letras.indexOf(simbolo) != -1)
      return true;
    return false;
  }

  public boolean CheckOperador(char simbolo){ // Método que verifica se o caracter dado é um operador
    String operadores = "*+-/";
    if (operadores.indexOf(simbolo) != -1)
        return true;
    return false;
  }

  public boolean CheckErro(String infixa){ // Método que verifica se na expressão têm erros
    // Verifica se a expressão têm símbolo inválidos
    String simbolosAceitos = "ABCDEFGHIJKLMNOPQRSTUVWXYZ*-+^/()";
    for (int i=0;i<infixa.length();i++){
      if (simbolosAceitos.indexOf(infixa.charAt(i)) == -1)
        return false;
    }
    // Verifica se os parênteses estão corretos
    int i = infixa.length()-1;
    Stack<Character> vet = new Stack<Character>();
    int count = 0;
    while (i>=0){
      char simbolo = infixa.charAt(i);
      if (simbolo == ')'){
        vet.push(simbolo);
        count++;
      }
      else if ((simbolo == '(') && count == 0)
        return false;
      else if (simbolo == '(' && ')' != vet.pop())
          return false;      
      i--;
    }
    if (count >0)
      return false;
    return true;
  }

  private int prioridade(char letra) {
    if ((letra == '+') || (letra == '-')){ return 1;}
    else if ((letra == '/') || (letra == '*')) { return 2;}
    else if (letra == '^') { return 3;}
    else {return -1;}
  }

  public String InPos(String expressao){ // Método que transforma a expressão de infixa para posfixa
    Stack<Character> stack = new Stack<Character>();
    String saida="";
    int count = 0;
    for (int i=0;i<expressao.length();i++){ // Loop que percorre a expressão
      char simbolo = expressao.charAt(i);
      if (CheckLetra(simbolo)){ // Verifica se é uma letra, se for, coloca na saída
        saida += simbolo;
      } else if (simbolo == '(') { // se for '(' coloca na pilha
        count++;
        stack.push(simbolo);
      } else if (simbolo == ')') { // se for ')' da coloca no saida o que estava na pilha ate achar o '('
        while (count-1 >= 0 && stack.peek() != '(') {
          saida += stack.pop();
          count--;
        }
        if (count-1 < 0 || stack.peek() != '(') { // verifica se não é possivel tal façanha 
          return "ERRO"; // caso sim retorna a mensagem "erro"
        } else {
          stack.pop();// caso não reduz o topo 
          count--;        
        }
      } else { // caso não ter mais nada
        while (count-1 >= 0 && prioridade(simbolo) <= prioridade(stack.peek())) { // coloca na saida o que sobrou de acordo com a prioridade
          saida += stack.pop();
          count--;
        }
        count++;
        stack.push(simbolo);
      }
    }
    while (count-1 >= 0) {
      saida += stack.pop(); // coloca na saida o que sobrou
      count--;
    }
    return saida;
  }

  public String Variaveis(String posfixa){ // Método que retorna as variáveis utilizadas na expressão, sem repetição
    String variaveis = "";
    for (int i=0;i<posfixa.length();i++){
      char simbolo = posfixa.charAt(i);
      if (CheckLetra(simbolo)){
        if (variaveis.indexOf(simbolo) == -1){
          variaveis += simbolo;
          }
        }
      }
    return variaveis;
  }

  public double Resultado(String posfixa, String variaveis, int[] valores){ // Método que calcula o Resultado da expressão dada
    Stack<Double> pilha = new Stack<Double>();
    for (int i=0;i<posfixa.length();i++){ // Loop que percorre a expressão inteira  
      double valor1, valor2;
      char simbolo = posfixa.charAt(i);
      if (CheckLetra(simbolo)){ // Verifica se é uma letra, se for, insere na pilha
        pilha.push((double) valores[variaveis.indexOf(simbolo)]);
      }
      else if (simbolo == '+'){ // Verifica se é uma soma, se for, empilha a soma dos 2 valores do peeko
        pilha.push(pilha.pop()+pilha.pop());
      }
      else if (simbolo == '-'){ // Verifica se é uma subtração, se for, empilha a subtração dos 2 valores do peeko
        valor1 = pilha.pop();
        valor2 = pilha.pop();
        pilha.push(valor2-valor1);
      }
      else if (simbolo == '*'){ // Verifica se é uma multiplicação, se for, empilha a multiplicação dos 2 valores do peeko
        pilha.push(pilha.pop()*pilha.pop());
      }
      else if (simbolo == '/'){ // Verifica se é uma divisão, se for, empilha a divisão dos 2 valores do peeko
        valor1 = pilha.pop();
        valor2 = pilha.pop();
        pilha.push(valor2/valor1);
      }
      else if (simbolo == '^'){ // Verifica se é uma potência, se for, empilha o Resultado da potência dos 2 valores do peeko
        valor1 = pilha.pop();
        valor2 = pilha.pop();
        pilha.push(Math.pow(valor2,valor1));
      }
    }
    return pilha.pop(); // Retorna o saida de toda a expressão
  }
  
}

class Main {
    public static void main(String[] args) {
        Scanner scane = new Scanner(System.in); // inicia o scanner
        System.out.println("Escolha o número: ");
        System.out.println("1. para colocar a expressão infixa");
        System.out.println("2. para colocar os valores das variáveis");
        System.out.println("3. para converter a expressão para sufixa");
        System.out.println("4. para apresentação do resultado");
        System.out.println("5. para finalizar o programa");
        int escolha = scane.nextInt(); // salva a escolha
        scane.nextLine(); // ignora o enter
        Operacao operacao = new Operacao(); // cria inicialmente a classe
        // Inicializa as variáveis
        double resp = Double.NaN;
        String infixa = "";
        String posfixa = "";
        String variaveis = "";
        int[] valores = new int[1];
        while (true){ // Loop infinito até que o usuário escolha encerrar o programa
            
            // Se a escolha for 1
            if (escolha == 1) { 
                // Pede a expressão e formata, salvando na variável infixa
                System.out.print("Digite a expresão: ");
                infixa = scane.nextLine();
                infixa = infixa.replaceAll(" ","");
                infixa = infixa.toUpperCase();

            // Se a escolha for 2
            } else if (escolha == 2) {  

                // Se a expressão já foi informada, pede os valores das variaveis e salva no vetor valores
                if (infixa != "" && operacao.CheckErro(infixa)) {
                    variaveis = operacao.Variaveis(infixa);
                    valores = new int[variaveis.length()];
                    for (int i=0;i<variaveis.length();i++){
                        System.out.println("Digite valor de " + variaveis.charAt(i) + ":");
                        valores[i] = scane.nextInt();
                    }  
                } else { // Se não foi informado a expressão ainda
                    System.out.println("ERRO na expressão");
                }
            
            // Se a escolha for 3
            } else if (escolha == 3) { 
                if (infixa != "") {
                    posfixa = operacao.InPos(infixa);
                    System.out.println(posfixa); // Printa a expressão na forma posfixa
                } else {
                    System.out.println("ERRO na expressão");
                }
            
            // Se a escolha for 4
            } else if (escolha == 4) { 
                if (variaveis != "" && infixa != "" && operacao.CheckErro(infixa)){
                    posfixa = operacao.InPos(infixa);
                    resp = operacao.Resultado(posfixa, variaveis, valores); // Calcula o resultado da expressão
                    System.out.println("Resultado da expressão: " + resp); 
                } else {
                    System.out.println("Ainda não foi informado o necessário");
                }

            // Se a escolha for 5
            } else if (escolha == 5){  
                System.out.println("Programa encerrado");  
                break; // para o programa
            }
            // continua o looping
            System.out.println("Escolha o número: ");
            System.out.println("1. para colocar a expressão infixa");
            System.out.println("2. para colocar os valores das variáveis");
            System.out.println("3. para converter a expressão para sufixa");
            System.out.println("4. para apresentação do resultado");
            System.out.println("5. para finalizar o programa");
            escolha = scane.nextInt();
            scane.nextLine();
        }
        scane.close();
    }
}