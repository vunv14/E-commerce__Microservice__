//package com.example.commonservice.configKafka;
//
//import lombok.AllArgsConstructor;
//import org.springframework.boot.autoconfigure.AutoConfiguration;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
//import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
//import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
//import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
//import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
//
//@AutoConfiguration // Đánh dấu lớp này là một auto-configuration class.
//@ConditionalOnClass({ KafkaTemplate.class }) // Đảm bảo auto-config này chỉ được tải nếu KafkaTemplate có sẵn, tức là khi Spring Kafka dependency được thêm vào.
//@EnableConfigurationProperties(KafkaProperties.class)
//@AllArgsConstructor
//public class CommonKafkaAutoConfiguration {
//
//    private final KafkaProperties kafkaProperties;
//
//    @Bean
//    @ConditionalOnMissingBean // Bean này chỉ được tạo nếu chưa có một bean ProducerFactory nào khác đã tồn tại trong ngữ cảnh Spring.
//    public ProducerFactory<String, String> kafkaProducerFactory() {
//        Map<String, Object> props = new HashMap<>(); // Tạo một Map để chứa các thuộc tính cấu hình cho Producer.
//        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()); // Đặt địa chỉ Kafka broker từ KafkaProperties.
//        props.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer"); // Chỉ định class dùng để serialize key của tin nhắn thành byte.
//        props.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer"); // Chỉ định class dùng để serialize value của tin nhắn thành byte.
//        // Thêm các cấu hình tùy chỉnh từ application.properties (common.kafka.producer.*)
//        props.putAll(kafkaProperties.getProducer()); // Kết hợp các thuộc tính mặc định với bất kỳ thuộc tính producer tùy chỉnh nào từ file cấu hình.
//        return new DefaultKafkaProducerFactory<>(props); // Trả về một DefaultKafkaProducerFactory mới với các cấu hình đã cho.
//    }
//
//    @Bean // Đánh dấu phương thức này tạo ra một Spring bean.
//    @ConditionalOnMissingBean // Bean này chỉ được tạo nếu chưa có một bean KafkaTemplate nào khác đã tồn tại.
//    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> kafkaProducerFactory) {
//        return new KafkaTemplate<>(kafkaProducerFactory); // Tạo một KafkaTemplate mới, sử dụng ProducerFactory đã được định nghĩa ở trên.
//    }
//
//    // --- Cấu hình Consumer ---
//    @Bean // Đánh dấu phương thức này tạo ra một Spring bean.
//    @ConditionalOnMissingBean // Bean này chỉ được tạo nếu chưa có một bean ConsumerFactory nào khác đã tồn tại.
//    public ConsumerFactory<String, String> kafkaConsumerFactory() {
//        Map<String, Object> props = new HashMap<>(); // Tạo một Map để chứa các thuộc tính cấu hình cho Consumer.
//        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()); // Đặt địa chỉ Kafka broker.
//        props.put(KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer"); // Chỉ định class dùng để deserialize key của tin nhắn từ byte.
//        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer"); // Chỉ định class dùng để deserialize value của tin nhắn từ byte.
//        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest"); // Ví dụ cấu hình mặc định: nếu không có offset được lưu trữ, bắt đầu đọc từ đầu (earliest) hoặc từ cuối (latest).
//        // Thêm các cấu hình tùy chỉnh từ application.properties (common.kafka.consumer.*)
//        props.putAll(kafkaProperties.getConsumer()); // Kết hợp các thuộc tính mặc định với bất kỳ thuộc tính consumer tùy chỉnh nào từ file cấu hình.
//        return new DefaultKafkaConsumerFactory<>(props); // Trả về một DefaultKafkaConsumerFactory mới với các cấu hình đã cho.
//    }
//
//    // Cấu hình ConcurrentKafkaListenerContainerFactory cho các KafkaListener
//    @Bean
//    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory") // Đảm bảo không ghi đè nếu đã có một bean tên "kafkaListenerContainerFactory".
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
//            ConsumerFactory<String, String> kafkaConsumerFactory) { // Phương thức nhận ConsumerFactory làm tham số (Spring sẽ tự động inject).
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>(); // Tạo một instance của factory.
//        factory.setConsumerFactory(kafkaConsumerFactory); // Gán ConsumerFactory đã được định nghĩa ở trên cho factory.
//        // Có thể thêm các cấu hình mặc định khác, ví dụ:
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD); // Đặt chế độ xác nhận (acknowledgement) là RECORD, nghĩa là listener sẽ gửi ACK sau khi xử lý xong mỗi bản ghi.
//        // factory.setConcurrency(3); // Số lượng luồng xử lý đồng thời cho các listener. (Đang được comment, có thể bỏ comment để kích hoạt)
//        return factory; // Trả về factory đã được cấu hình.
//    }
//
//
//}
