package gg.amy.catnip.rx;

import com.mewna.catnip.shard.event.EventType;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author amy
 * @since 1/16/19.
 */
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CatnipEventStream<T> implements ReadStream<T> {
    @Getter
    private final EventType<T> type;
    private final Deque<T> buffer = new LinkedList<>();
    
    private boolean paused;
    
    private Handler<T> handler;
    private Handler<Throwable> exceptionHandler;
    
    public static <E> CatnipEventStream<E> create(final EventType<E> type) {
        return new CatnipEventStream<>(type);
    }
    
    public void write(@Nonnull final T event) {
        try {
            if(paused) {
                buffer.addLast(event);
            } else {
                handler.handle(event);
            }
        } catch(final Exception e) {
            exceptionHandler.handle(e);
        }
    }
    
    public ReadStream<T> exceptionHandler(final Handler<Throwable> handler) {
        exceptionHandler = handler;
        return this;
    }
    
    public ReadStream<T> handler(final Handler<T> handler) {
        this.handler = handler;
        return this;
    }
    
    public ReadStream<T> pause() {
        paused = true;
        return this;
    }
    
    public ReadStream<T> resume() {
        paused = false;
        try {
            if(!buffer.isEmpty()) {
                // Assuming that they might update pause-state from another thread
                while(!buffer.isEmpty() && !paused) {
                    handler.handle(buffer.poll());
                }
            }
        } catch(final Exception e) {
            exceptionHandler.handle(e);
        }
        return this;
    }
    
    public ReadStream<T> fetch(final long l) {
        long counter = l;
        try {
            while(!buffer.isEmpty() && counter > 0) {
                handler.handle(buffer.poll());
                --counter;
            }
        } catch(final Exception e) {
            exceptionHandler.handle(e);
        }
        return this;
    }
    
    public ReadStream<T> endHandler(final Handler<Void> handler) {
        // We don't bother w/ this since the stream doesn't end unless the
        // bot is stopped entirely, which generally just means that the
        // application as a whole has stopped.
        return this;
    }
}
