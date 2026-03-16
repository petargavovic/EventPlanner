/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.events.planner.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
/**
 *
 * @author MAU
 */
@Route("")
@PageTitle("Planner")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    public MainView() {
        add(
            new H1("Planner UI"),
            new com.vaadin.flow.component.html.Paragraph("Vaadin is working.")
        );
    }
}
