import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        //Получаем ссылку на файл с исходными данными
        var uri = Objects.requireNonNull(Main.class.getResource(String.format("/%s", "tickets.json"))).toURI();

        //Читаем файл с данными
        String bodyFile = Files.readString(Path.of(uri));

        //Файл содержит символ не соответствующий спецификации json, удаляем этот символ
        String formattedString = bodyFile.trim().replaceAll("\ufeff", "");

        //Парсим json
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        Map<String, List<Flights>> map = objectMapper.readValue(formattedString, new TypeReference<>() {
        });

        //Получаем список полетов
        List<Flights> flights = map.get("tickets");

        //Фильтруем только полеты Владивосток - Тель-Авив
        List<Flights> filterFlightsVladivostokTelAviv = flights.stream()
                .filter(c -> c.getOrigin_name().equals("Владивосток") && c.getDestination_name().equals("Тель-Авив"))
                .toList();

        //Получаем массив цен на билеты Владивосток - Тель-Авив
        int[] prices = new int[filterFlightsVladivostokTelAviv.size()];
        for (int i = 0; i < filterFlightsVladivostokTelAviv.size(); i++) {
            prices[i] = filterFlightsVladivostokTelAviv.get(i).getPrice();
        }

        //Получаем перечень авиакомпаний
        Set<String> carriers = new HashSet<>();
        for (var flight : filterFlightsVladivostokTelAviv) {
            carriers.add(flight.getCarrier());
        }

        //Получаем карту, где каждой авиакомпании соответсвует число в минутах, равное минимальному времени полёта
        Map<String, Long> minTimes = new HashMap<>();
        for (var carrier : carriers) {
            long timeOfFlight = filterFlightsVladivostokTelAviv.stream()
                    .filter(c -> carrier.equals(c.getCarrier()))
                    .mapToLong(c -> {
                        //Время вылета из Владивостока (по Владивостокскому времени)
                        LocalDateTime departure = LocalDateTime.of(c.getDeparture_date(), c.getDeparture_time());
                        //Время вылета из Владивостока (по Тель-Авивскому времени)
                        LocalDateTime departureTelAvivLocale = departure.minus(7, ChronoUnit.HOURS);
                        //Время прибытия в Тель-Авив (по Тель-Авивскому времени)
                        LocalDateTime arrival = LocalDateTime.of(c.getArrival_date(), c.getArrival_time());
                        //Возвращаем разницу между вылетом и прилётом в минутах по Тель-Авивскому времени
                        return departureTelAvivLocale.until(arrival, ChronoUnit.MINUTES);
                    })
                    //Находим минимальное значение времени полета
                    .min().getAsLong();
            minTimes.put(carrier, timeOfFlight);
        }

        //Печатаем результат в соответствии с заданием, так как нет требований
        // к единицам измерения времени полёта, результат приведен в минутах
        // если есть какие-то требования к тому в каком виде должен быть результат, можно добавить метод по конвертации
        // количества минут в требуемый вид
        System.out.println("Минимальное время полета между городами Владивосток и Тель-Авив для каждого" +
                " авиаперевозчика:");
        for (var pair : minTimes.entrySet()) {
            System.out.println("Авиакомпания - " + pair.getKey() + " Минимальное время полёта - " + pair.getValue() +
                    " минут");
        }
        System.out.println("Разница между средней ценой  и медианой для полета между городами  Владивосток и" +
                " Тель-Авив: " + (findAverage(prices) - findMedian(prices)));
    }

    //Метод вычисления медианы
    public static double findMedian(int[] prices) {
        Arrays.sort(prices);
        int n = prices.length;
        if (n % 2 == 0) {
            return (prices[n / 2 - 1] + prices[n / 2]) / 2.0;
        } else {
            return prices[n / 2];
        }
    }


    //Метод вычисления среднего
    public static double findAverage(int[] prices) {
        OptionalDouble averagePrices = IntStream.of(prices).average();
        return averagePrices.getAsDouble();
    }
}
