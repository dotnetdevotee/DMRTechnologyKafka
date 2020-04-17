package com.dmrtech.endstate.controllers;

import com.dmrtech.endstate.model.CalendarItem;
import com.dmrtech.endstate.model.CalendarItemEvent;
import com.dmrtech.endstate.payload.request.CalendarItemRequest;
import com.dmrtech.endstate.services.CalendarItemEventProducer;
import com.dmrtech.endstate.services.CalendarItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/calendarevents")
public class CalendarItemCommandController {

    private final CalendarItemService calendarItemService;
    private final CalendarItemEventProducer calendarItemEventProducer;

    public CalendarItemCommandController(CalendarItemService calendarItemService, CalendarItemEventProducer calendarItemEventProducer) {
        this.calendarItemService = calendarItemService;
        this.calendarItemEventProducer = calendarItemEventProducer;
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('USER')")
    public void calendarItem(@RequestBody CalendarItemRequest calendarItemRequest) {
        CalendarItem calendarItem = calendarItemService.generateFromRequest(calendarItemRequest);

        CalendarItemEvent newCalendarItemEvent = new CalendarItemEvent(CalendarItemEvent.EVENT_TYPE.CREATED, calendarItem);
        calendarItemEventProducer.sendMessage( newCalendarItemEvent );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public void update(@RequestBody CalendarItemRequest calendarItemRequest, @PathVariable Long id) {

        // send event
        CalendarItem updatedCalendarItem = calendarItemService.generateFromRequest(calendarItemRequest);

        CalendarItemEvent updatedCalendarItemEvent = new CalendarItemEvent(CalendarItemEvent.EVENT_TYPE.UPDATED, updatedCalendarItem);
        calendarItemEventProducer.sendMessage(updatedCalendarItemEvent);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public void delete(@PathVariable Long id) {
        CalendarItem calendarItem = new CalendarItem();
        calendarItem.setId(id);
        CalendarItemEvent calendarItemEvent = new CalendarItemEvent(CalendarItemEvent.EVENT_TYPE.DELETED,calendarItem );
        calendarItemEventProducer.sendMessage(calendarItemEvent);
    }

}
