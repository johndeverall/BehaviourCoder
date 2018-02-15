---
layout: page
title: Manual
permalink: /manual/
---

# What is the Simple Behaviour Coder?

The Simple Behaviour Coder is a video behaviour analysis tool. It is currently configured for a particular Rooster Study, but has been previously configured for a study using Indian Mynas.

# Future development

The intention is to make behaviour coder generic, modular and configurable so that it can be applied to a range of studies and species.

# Features required for generalisation

#### 1. A generic location panel, where mutually exclusive timer buttons can be created to show

* how many transitions occur
* when transitions occur in the chronology of a trial
* what the transitions were (i.e. from region A to region D)

Each timer button should have:

* *An optional label* - that shows which region is being recorded
* *A header* - used as a column header when the information from this timer button is saved in an excel spreadsheet.
* Like the current timer buttons, the generic timer buttons will show the time counted in hundredths of a second.

#### 2. A generic counter panel, where clicker buttons can be created to record events / actions both in number and where they occur in the chronology of a trial

Each counter button should have:

* *An optional label* - that shows the name of the action associated with the counter button
* *An optional colour* - to make each action button stand out if required
* *A header* - used as a column header when the information from this counter button is saved in an excel spreadsheet.
* *An associate with location panel option* So that each event that occurs can be recorded as happening in a location. If this option is checked, then the header used in excel will be a combination of the location button header and the counter button header.

#### 3. A generic behaviour timer, which is used to recorder the duration of a behaviour, when it started, and when it stopped in the chronology of a trial.

Each counter button should have:

* *An optional label* - that shows the name of the action associated with the timer button
* *An optional colour* - to make each action button stand out if required
* *A header* - used as a column header when the information from this counter button is saved in an excel spreadsheet.
* *An associate with location panel option* So that each event that occurs can be recorded as happening in a location. If this option is checked, then the header used in excel will be a combination of the location button header and the counter button header.
* These timer buttons should also count the number of times it has toggled on (been started) - this could be optionally shown on the button (perhaps smaller, in the corner, and grey, so as not to distract from the timer)

#### 4. Additional desired features (not yet implemented)

* tabs/sections for tracking different focal animals
* visible video tracking slider which shows an overlay of the audio
* parallel to the video slider - colored bars indicating the buttons you are using to track actions/location/events in real time