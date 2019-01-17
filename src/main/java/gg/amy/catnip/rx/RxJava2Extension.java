package gg.amy.catnip.rx;

import com.mewna.catnip.extension.AbstractExtension;
import com.mewna.catnip.shard.EventType;
import io.reactivex.Observable;
import io.vertx.reactivex.ObservableHelper;

/**
 * @author amy
 * @since 1/16/19.
 */
@SuppressWarnings("unused")
public class RxJava2Extension extends AbstractExtension {
    public RxJava2Extension() {
        super("rx-java-2");
    }
    
    public <T> Observable<T> createObservable(final EventType<T> type) {
        final CatnipEventStream<T> stream = CatnipEventStream.create(type);
        on(type, stream::write);
        return ObservableHelper.toObservable(stream);
    }
}
