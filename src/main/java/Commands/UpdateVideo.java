package Commands;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import Model.Videos;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public class UpdateVideo extends Command {

    public void execute() {
        HashMap<String, Object> props = parameters;
        System.out.println(parameters);
        Channel channel = (Channel) props.get("channel");
        JSONParser parser = new JSONParser();

        try {
            JSONObject body = (JSONObject) parser.parse((String) props.get("body"));
            JSONObject params = (JSONObject) parser.parse(body.get("body").toString());
            AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
            AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
            Envelope envelope = (Envelope) props.get("envelope");
            String response = Videos.updateVideo(params);
            try {
                channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
