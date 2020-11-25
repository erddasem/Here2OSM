# How to set up routable OSM database

Instructions for setting up a routable PostgreSQL database

## Table of Contents
1. [General Info](#general-info)
2. [Programms and extensions needed](#programms)
3. [Get OSM data](#osmdata)
4. [Setup database](#database_setup)
6. [FAQs](#faqs)

### General Info
***
This guide shows you how to set up a usable PostgreSQL database for the reference implementation at hand.

### Programms and extensions needed
***
#### Programms
+ [PostgreSQL database](https://www.postgresql.org)
+ [OSM2PGSQL](https://osm2pgsql.org)
+ Database client, e.g. [DBeaver](https://dbeaver.com), [pgAdmin](https://www.pgadmin.org)
#### Extensions
+ [PostGIS](http://postgis.net)
+ [pgRouting](https://pgrouting.org)
+ [hstore](hstore)

### Get OSM data 
***
Load [OpenStreetMap](https://www.openstreetmap.org/#map=6/51.330/10.453) from the OSM Exporter or the [Geofabrik Downloader](https://download.geofabrik.de). The data must be a *.osm.pdf file. 

For example: hamburg-latest.osm.pbf

### Setup database
***
1. [Download](https://www.postgresql.org/download/) PostgreSQL and add PostGIS extension.
2. [Create](https://www.postgresql.org/docs/9.0/tutorial-createdb.html) database
3. Run the following commands in you database client: 

+ Create postgis extension
  ```sql 
  CREATE EXTENSION postgis;
  ```
+ Create pgRouting extension
  ```sql 
  CREATE EXTENSION pgrouting;
  ```
+ Create hstore extension
  ```sql 
  CREATE EXTENSION hstore;
  ```
4. Load OSM data in your databse using terminal
```bash
Osm2pgsql -d dbname -U username osmpbffilename.osm.pbf --hstore
```
If you have a larger file, e.g. OSM data for Germany use the following command: 
```bash
--Osm2pgsql -d dbname -U username osmpbffilename.osm.pbf --slim --hstore
```
