/**
 * Package for laboration 3, the third assignment of course DT181G.
 *
 * The sprite editor consists of a model, a view and a controller.
 *
 *  View: All JComponents
 *  Controller: Handles commands from view and model. Load/save functionality.
 *  Model: Logic for loading saving files when told so by controller. Model for sprite entity.
 *
 *  By the nature of being a graphically oriented program, much logic would seem to be located to the
 *  render panel, but this is in fact mainly helper methods for doing vector projections.
 *
 *  In theory however, sprites could be constructed, then normalized, calculated and build in model before sent to renderer
 *  to separate concerns better. But due to scope creeping this was not feasible.
 *
 *  The hope is that this program can be utilized for sprite construction in the upcoming project.
 */
package com.m1tron.fractal;
