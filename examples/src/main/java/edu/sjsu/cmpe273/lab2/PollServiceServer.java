package edu.sjsu.cmpe273.lab2;

import io.grpc.ServerImpl;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;

import java.util.logging.Logger;

/**
 * Created by anupkher on 3/18/15.
 */

public class PollServiceServer {
  private static final Logger logger = Logger.getLogger(PollServiceServer.class.getName());

  private int port = 50051;
  private ServerImpl server;

  private void start() throws Exception {
    server = NettyServerBuilder.forPort(port)
        .addService(PollServiceGrpc.bindService(new PollServiceImpl()))
        .build().start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        PollServiceServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws Exception {
    final PollServiceServer server = new PollServiceServer();
    server.start();
  }

  private class PollServiceImpl implements PollServiceGrpc.PollService {

    @Override
    public void createPoll(PollRequest req, StreamObserver<PollResponse> responseObserver) {
      //PollResponse reply = PollResponse.newBuilder().setMessage("Hello " + req.getName()).build();
      PollResponse reply = PollResponse.newBuilder().setId("1ABZ4F").build();
      responseObserver.onValue(reply);
      logger.info("Moderator Id for this request:" + " " + req.getModeratorId());
      responseObserver.onCompleted();
    }
  }

}
