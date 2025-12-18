package zk.discovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.InstanceSerializer;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 实例元数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstanceDetails {

    private String description;

    // ------------------------------------------------

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    public static final Type TYPE = new TypeToken<ServiceInstance<InstanceDetails>>() {}.getType();
    public static final InstanceSerializer<InstanceDetails> INSTANCE_SERIALIZER = new InstanceSerializer<>() {
        @Override
        public byte[] serialize(ServiceInstance<InstanceDetails> serviceInstance) {
            return GSON.toJson(serviceInstance).getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public ServiceInstance<InstanceDetails> deserialize(byte[] bytes) {
            return GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), TYPE);
        }
    };
}
