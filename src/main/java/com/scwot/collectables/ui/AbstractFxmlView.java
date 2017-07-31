/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scwot.collectables.ui;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.util.ResourceBundle.getBundle;

public abstract class AbstractFxmlView implements ApplicationContextAware {

    private static final String MAIN_FXML = "/main.fxml";
    private static final String RESOURCE_BUNDLE = "projects";
    private ObjectProperty<Object> presenterProperty;
    private FXMLLoader fxmlLoader;
    private ResourceBundle bundle;
    private URL resource;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        if (this.applicationContext != null) {
            return;
        }

        this.applicationContext = applicationContext;
    }

    public AbstractFxmlView() {
        presenterProperty = new SimpleObjectProperty<>();
        resource = getClass().getResource(MAIN_FXML);
        bundle = getResourceBundle(RESOURCE_BUNDLE);
    }

    private Object createControllerForType(Class<?> type) {
        return this.applicationContext.getBean(type);
    }

    FXMLLoader loadSynchronously(URL resource, ResourceBundle bundle) throws IllegalStateException {

        final FXMLLoader loader = new FXMLLoader(resource, bundle);
        loader.setControllerFactory(this::createControllerForType);

        try {
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot load " + getConventionalName(), ex);
        }

        return loader;
    }

    void ensureFxmlLoaderInitialized() {

        if (this.fxmlLoader != null) {
            return;
        }

        this.fxmlLoader = loadSynchronously(resource, bundle);
        this.presenterProperty.set(this.fxmlLoader.getController());
    }

    public Parent getView() {
        ensureFxmlLoaderInitialized();

        final Parent parent = fxmlLoader.getRoot();
        addCSSIfAvailable(parent);
        return parent;
    }

    /**
     * Initializes the view synchronously and invokes the consumer with the created parent Node within the FX UI thread.
     *
     * @param consumer - an object interested in received the {@link Parent} as callback
     */
    public void getView(Consumer<Parent> consumer) {
        CompletableFuture.supplyAsync(this::getView, Platform::runLater).thenAccept(consumer);
    }

    /**
     * Scene Builder creates for each FXML document a root container. This method omits the root container (e.g.
     * {@link AnchorPane}) and gives you the access to its first child.
     *
     * @return the first child of the {@link AnchorPane}
     */
    public Node getViewWithoutRootContainer() {

        ObservableList<Node> children = getView().getChildrenUnmodifiable();
        if (children.isEmpty()) {
            return null;
        }

        return children.listIterator().next();
    }

    private void addCSSIfAvailable(Parent parent) {
        URL uri = getClass().getResource(getStyleSheetName());
        if (uri == null) {
            return;
        }

        String uriToCss = uri.toExternalForm();
        parent.getStylesheets().add(uriToCss);
    }

    private String getStyleSheetName() {
        return getConventionalName(".css");
    }

    /**
     * In case the view was not initialized yet, the conventional fxml (airhacks.fxml for the AirhacksView and
     * AirhacksPresenter) are loaded and the specified presenter / controller is going to be constructed and returned.
     *
     * @return the corresponding controller / presenter (usually for a AirhacksView the AirhacksPresenter)
     */
    public Object getPresenter() {

        ensureFxmlLoaderInitialized();

        return this.presenterProperty.get();
    }

    /**
     * Does not initialize the view. Only registers the Consumer and waits until the the view is going to be created / the
     * method FXMLView#getView or FXMLView#getViewAsync invoked.
     *
     * @param presenterConsumer listener for the presenter construction
     */
    public void getPresenter(Consumer<Object> presenterConsumer) {

        this.presenterProperty.addListener((ObservableValue<? extends Object> o, Object oldValue, Object newValue) -> {
            presenterConsumer.accept(newValue);
        });
    }

    /**
     * @param ending the suffix to append
     * @return the conventional name with stripped ending
     */
    protected String getConventionalName(String ending) {
        return getConventionalName() + ending;
    }

    /**
     * @return the name of the view without the "View" prefix in lowerCase. For AirhacksView just airhacks is going to be
     * returned.
     */
    protected String getConventionalName() {
        return stripEnding(getClass().getSimpleName().toLowerCase());
    }

    String getBundleName() {
        return getClass().getPackage().getName() + "." + getConventionalName();
    }

    static String stripEnding(String clazz) {

        if (!clazz.endsWith("view")) {
            return clazz;
        }

        return clazz.substring(0, clazz.lastIndexOf("view"));
    }

    private ResourceBundle getResourceBundle(String name) {
        try {
            return getBundle(name);
        } catch (MissingResourceException ex) {
            return null;
        }
    }
}
