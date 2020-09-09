package fr.mnhn.diversity.common.testing;

import com.ninja_squad.dbsetup.DbSetupTracker;

/**
 * A singleton class for the DbSetup tracker
 * @author JB Nizet
 */
public class Tracker {
    public static final DbSetupTracker TRACKER = new DbSetupTracker();
}
