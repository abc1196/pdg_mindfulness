package mindfulness.pdg_mindfulness.utils.interfaces;

import mindfulness.pdg_mindfulness.dashboard.data.User;

public interface DashboardNavigationHost {
    /**
     * Trigger a navigation to the specified fragment, optionally adding a transaction to the back
     * stack to make this navigation reversible.
     */

    void logout();
    void goToTreatment(int dayNumber);
    User getUserStats();
}
