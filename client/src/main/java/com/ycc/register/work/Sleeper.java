package com.ycc.register.work;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sleeper for current thread.
 * Sleeps for passed period.  Also checks passed boolean and if interrupted,
 * will return if the flag is set (rather than go back to sleep until its
 * sleep time is up).
 */
public class Sleeper {
  private final Log LOG = LogFactory.getLog(this.getClass().getName());
  private final int period;
  private final Stoppable stopper;
  private static final long MINIMAL_DELTA_FOR_LOGGING = 10000;

  private final Object sleepLock = new Object();
  private boolean triggerWake = false;

  /**
   * @param sleep sleep time in milliseconds
   * @param stopper When {@link Stoppable#isStopped()} is true, this thread will
   * cleanup and exit cleanly.
   */
  public Sleeper(final int sleep, final Stoppable stopper) {
    this.period = sleep;
    this.stopper = stopper;
  }

  /**
   * Sleep for period.
   */
  public void sleep() {
    sleep(System.currentTimeMillis());
  }

  /**
   * If currently asleep, stops sleeping; if not asleep, will skip the next
   * sleep cycle.
   */
  public void skipSleepCycle() {
    synchronized (sleepLock) {
      triggerWake = true;
      sleepLock.notifyAll();
    }
  }

  /**
   * Sleep for period adjusted by passed <code>startTime<code>
   * @param startTime Time some task started previous to now.  Time to sleep
   * will be docked current time minus passed <code>startTime<code>.
   */
  public void sleep(final long startTime) {
    if (this.stopper.isStopped()) {
      return;
    }
    long now = System.currentTimeMillis();
    long waitTime = this.period - (now - startTime);
    if (waitTime > this.period) {
      LOG.warn("Calculated wait time > " + this.period +
        "; setting to this.period: " + System.currentTimeMillis() + ", " +
        startTime);
      waitTime = this.period;
    }
    while (waitTime > 0) {
      long woke = -1;
      try {
        synchronized (sleepLock) {
          if (triggerWake) break;
          sleepLock.wait(waitTime);
        }
        woke = System.currentTimeMillis();
        long slept = woke - now;
        if (slept - this.period > MINIMAL_DELTA_FOR_LOGGING) {
          LOG.warn("We slept " + slept + "ms instead of " + this.period +
              "ms, this is likely due to a long " +
              "garbage collecting pause and it's usually bad, see " +
              "http://hbase.apache.org/book.html#trouble.rs.runtime.zkexpired");
        }
      } catch(InterruptedException iex) {
        // We we interrupted because we're meant to stop?  If not, just
        // continue ignoring the interruption
        if (this.stopper.isStopped()) {
          return;
        }
      }
      // Recalculate waitTime.
      woke = (woke == -1)? System.currentTimeMillis(): woke;
      waitTime = this.period - (woke - startTime);
    }
    triggerWake = false;
  }
}
