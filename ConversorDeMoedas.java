import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversorDeMoedas {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/738bd7253bc6cfbbf2a42feb/latest/USD";

    public static void main(String[] args) {
        try {
            Map<String, Double> rates = fetchRates();
            if (rates == null) {
                System.out.println("Falha ao obter taxas de conversão.");
                return;
            }

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Conversor de Moedas:");
                System.out.println("1. USD para EUR");
                System.out.println("2. USD para GBP");
                System.out.println("3. USD para JPY");
                System.out.println("4. USD para BRL");
                System.out.println("5. USD para CAD");
                System.out.println("6. USD para AUD");
                System.out.println("7. Sair");
                System.out.print("Escolha uma opção: ");
                int opcao = scanner.nextInt();

                if (opcao == 7) {
                    System.out.println("Saindo...");
                    break;
                }

                System.out.print("Digite o valor em USD: ");
                double valorUSD = scanner.nextDouble();
                double valorConvertido = 0;

                switch (opcao) {
                    case 1:
                        valorConvertido = converter(valorUSD, rates.get("EUR"));
                        System.out.println("Valor em EUR: " + valorConvertido);
                        break;
                    case 2:
                        valorConvertido = converter(valorUSD, rates.get("GBP"));
                        System.out.println("Valor em GBP: " + valorConvertido);
                        break;
                    case 3:
                        valorConvertido = converter(valorUSD, rates.get("JPY"));
                        System.out.println("Valor em JPY: " + valorConvertido);
                        break;
                    case 4:
                        valorConvertido = converter(valorUSD, rates.get("BRL"));
                        System.out.println("Valor em BRL: " + valorConvertido);
                        break;
                    case 5:
                        valorConvertido = converter(valorUSD, rates.get("CAD"));
                        System.out.println("Valor em CAD: " + valorConvertido);
                        break;
                    case 6:
                        valorConvertido = converter(valorUSD, rates.get("AUD"));
                        System.out.println("Valor em AUD: " + valorConvertido);
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
                System.out.println();
            }
            scanner.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Double> fetchRates() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonResponse = mapper.readValue(response.body(), Map.class);
            return (Map<String, Double>) jsonResponse.get("conversion_rates");
        } else {
            System.out.println("Erro ao acessar a API. Código de status: " + response.statusCode());
            return null;
        }
    }

    private static double converter(double valor, double taxa) {
        return valor * taxa;
    }
}
