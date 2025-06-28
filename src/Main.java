import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean continuar = true;

        while (continuar) {
            System.out.println("\n===== CONVERSOR DE MOEDAS =====");
            System.out.println("1. USD -> BRL");
            System.out.println("2. BRL -> USD");
            System.out.println("3. EUR -> BRL");
            System.out.println("4. BRL -> EUR");
            System.out.println("5. ARS -> BRL");
            System.out.println("6. CLP -> BRL");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();

            if (opcao == 0) {
                continuar = false;
                System.out.println("Até mais!");
                break;
            }

            String de = "", para = "BRL";

            switch (opcao) {
                case 1 -> de = "USD";
                case 2 -> {
                    de = "BRL";
                    para = "USD";
                }
                case 3 -> de = "EUR";
                case 4 -> {
                    de = "BRL";
                    para = "EUR";
                }
                case 5 -> de = "ARS";
                case 6 -> de = "CLP";
                default -> {
                    System.out.println("Opção inválida.");
                    continue;
                }
            }

            System.out.print("Digite o valor: ");
            double valor = scanner.nextDouble();

            double taxa = obterTaxa(de, para);
            if (taxa != -1) {
                double convertido = valor * taxa;
                System.out.printf("Resultado: %.2f %s = %.2f %s\n", valor, de, convertido, para);
            } else {
                System.out.println("Erro ao buscar a taxa de câmbio.");
            }
        }

        scanner.close();
    }

    private static double obterTaxa(String de, String para) {
        try {
            // Substitua aqui pela sua chave da API
            String chave = "6c26030b25b76bdb8836f21a";
            String url_str = "https://v6.exchangerate-api.com/v6/" + chave + "/latest/" + de;

            URL url = new URL(url_str);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;

            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }

            leitor.close();

            String json = resposta.toString();
            String chaveBusca = "\"" + para + "\":";
            int indice = json.indexOf(chaveBusca) + chaveBusca.length();
            int fim = json.indexOf(",", indice); // Ou fecha chave
            if (fim == -1) fim = json.indexOf("}", indice);
            String taxaStr = json.substring(indice, fim).trim();

            return Double.parseDouble(taxaStr);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return -1;
        }
    }
}