package edu.sjsu.cmpe273.lab2;

import io.grpc.ChannelImpl;
import io.grpc.transport.netty.NegotiationType;
import io.grpc.transport.netty.NettyChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by anupkher on 3/18/15.
 */
public class PollServiceClient {
    private static final Logger logger = Logger.getLogger(PollServiceClient.class.getName());

    private final ChannelImpl channel;
    private final PollServiceGrpc.PollServiceBlockingStub blockingStub;

    public PollServiceClient(String host, int port) {
        channel =
                NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.PLAINTEXT)
                        .build();
        blockingStub = PollServiceGrpc.newBlockingStub(channel);

    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTerminated(5, TimeUnit.SECONDS);
    }

    public void pollData() {

    }

    public void poll(String modId, String question, String started, String expired) {
        try {
            logger.info("Storing data on the server" + " ...");
            PollRequest request = PollRequest.newBuilder()
                                  .setModeratorId(modId)
                                  .setQuestion(question)
                                  .setStartedAt(started)
                                  .setExpiredAt(expired)
                                  .build();
            PollResponse response = blockingStub.createPoll(request);
            logger.info("Response from poll server: " + " " + "id-" + response.getId());
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "RPC failed", e);
            return;
        }
    }

    public static void main(String[] args) throws Exception {
        PollServiceClient client = new PollServiceClient("localhost", 50051);
        try {
      /* Access a service running on the local machine on port 50051 */
            String modId = "1";
            String question = "What type of smartphone do you have?";
            String started = "2015-02-23T13:00:00.000Z";
            String expired = "2015-02-23T13:00:00.000Z";
            String[] choice = new String[] {"Android, iPhone"};
            /*if (args.length > 0) {
                user = args[0]; /* Use the arg as the name to greet if provided */
            //}
            client.poll(modId, question, started, expired);
        } finally {
            client.shutdown();
        }
    }

}
