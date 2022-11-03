package ru.yandex.practicum.filmorate.test;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.yandex.practicum.filmorate.controller.AbstractController;
import ru.yandex.practicum.filmorate.model.Item;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AbstractControllerTest {
    /*
    private static AbstractController getController(boolean validationResult) {
        AbstractController controller = mock(AbstractController.class,
                withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        when(controller.validate(any(Item.class))).thenReturn(validationResult);

        return controller;
    }

    private static AbstractController getController() {
        return getController(true);
    }

    @Test
    public void testGetAllItems() {
        AbstractController controller = getController();
        Item item1 = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        Item item2 = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));

        controller.create(item1, null);
        controller.create(item2, null);

        assertEquals(List.of(item1, item2), controller.getAll());
    }

    @Test
    public void testCreation() {
        AbstractController controller = getController();
        Item item = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));

        assertSame(item, controller.create(item, null));
        assertEquals(1, item.getId());
    }

    @Test
    public void testCreationWhenInvalid() {
        AbstractController controller = getController(false);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        Item item = mock(Item.class);

        doNothing().when(response).setStatus(statusCaptor.capture());

        assertSame(item, controller.create(item, response));
        assertEquals(400, statusCaptor.getValue());
    }

    @Test
    public void testUpdating() {
        AbstractController controller = getController();
        Item item = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));

        controller.create(item, null);

        assertEquals(1, item.getId());
        assertSame(item, controller.update(item, null));
    }

    @Test
    public void testUpdatingWhenIdIsNull() {
        AbstractController controller = getController();
        Item item = mock(Item.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(response).setStatus(statusCaptor.capture());

        assertSame(item, controller.update(item, response));
        assertEquals(404, statusCaptor.getValue());
    }

    @Test
    public void testUpdatingWhenNotExists() {
        AbstractController controller = getController();
        Item item = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        HttpServletResponse response = mock(HttpServletResponse.class);
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(response).setStatus(statusCaptor.capture());
        item.setId(100);

        assertSame(item, controller.update(item, response));
        assertEquals(404, statusCaptor.getValue());
    }

    @Test
    public void testUpdatingWhenInvalid() {
        AbstractController controller = getController();
        Item item = mock(Item.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));

        controller.create(item, null);

        HttpServletResponse response = mock(HttpServletResponse.class);
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);

        doNothing().when(response).setStatus(statusCaptor.capture());
        when(controller.validate(any(Item.class))).thenReturn(false);

        assertSame(item, controller.update(item, response));
        assertEquals(400, statusCaptor.getValue());
    }

     */
}