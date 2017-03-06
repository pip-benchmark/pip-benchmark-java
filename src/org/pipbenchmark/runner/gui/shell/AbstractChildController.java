package org.pipbenchmark.runner.gui.shell;

public abstract class AbstractChildController {
    private MainController _mainController;

    protected AbstractChildController(MainController mainController) {
        _mainController = mainController;
    }

    public MainController getMainController() {
        return _mainController;
    }

    public IMainView getMainView() {
        return _mainController.getView();
    }
}
