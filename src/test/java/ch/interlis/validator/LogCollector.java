package ch.interlis.validator;

import java.util.ArrayList;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.logging.LogEvent;
import ch.interlis.iox.IoxLogEvent;
import ch.interlis.iox.IoxLogging;

public class LogCollector implements IoxLogging {
    private final ArrayList<IoxLogEvent> errs = new ArrayList<>();
    private final ArrayList<IoxLogEvent> warn = new ArrayList<>();

    @Override
    public void addEvent(IoxLogEvent event) {
        EhiLogger.getInstance().logEvent((LogEvent) event);
        if (event.getEventKind() == IoxLogEvent.ERROR) {
            errs.add(event);
        } else if (event.getEventKind() == IoxLogEvent.WARNING) {
            warn.add(event);
        }
    }

    public ArrayList<IoxLogEvent> getErrs() {
        return errs;
    }

    public ArrayList<IoxLogEvent> getWarn() {
        return warn;
    }
}
