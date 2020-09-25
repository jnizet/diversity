package fr.mnhn.diversity.common.testing;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;

import com.ninja_squad.dbsetup.DbSetupTracker;
import com.ninja_squad.dbsetup.operation.Operation;

/**
 * A singleton class for the DbSetup tracker and deleteAll operation
 * @author JB Nizet
 */
public class RepositoryTests {
    public static final DbSetupTracker TRACKER = new DbSetupTracker();

    public static final Operation DELETE_ALL =
        deleteAllFrom(
            "indicator_value",
            "indicator_category",
            "indicator_ecogesture",
            "indicator",
            "category",
            "ecogesture",
            "page_element",
            "page",
            "image",
            "app_user"
        );
}
