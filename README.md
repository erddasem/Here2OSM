# Here2OSM

## About

* Implementation to request traffic incident data from the provider HERE and map them on your own OpenStreetMap map database
* Uses the TomTom OpenLR and the TEPG2-OLR standards
* Project status: reference implementation

Necessary information:

* TomTom OpenLR implementation: <https://github.com/tomtom-international/openlr>
* HERE Traffic API: <https://developer.here.com/documentation/traffic/dev_guide/topics/incident-data.html>

## Table of contents

Use for instance <https://github.com/ekalinin/github-markdown-toc>:

> * [HERE2OSM](#title--repository-name)
>   * [About](#about)
>   * [Table of contents](#table-of-contents)
>   * [Code](#code)
>     * [Content](#content)
>     * [Requirements](#requirements)
>     * [Build](#build)
>     * [Deploy (how to install build product)](#deploy-how-to-install-build-product)
>   * [Resources (Documentation and other links)](#resources-documentation-and-other-links)
>   * [License](#license)

## Code

[![Build Status](https://qa.nuxeo.org/jenkins/buildStatus/icon?job=/nuxeo/addons_nuxeo-sample-project-master)](https://qa.nuxeo.org/jenkins/job/nuxeo/job/addons_nuxeo-sample-project-master/)

### Content

The implementation needs a PostgreSQL Database with PostGIS extension containing a routable OpenStreetMap road topology. 
A routing topology can be created with pgRouting. In the database the scheme openlr is needed. 
If you want to change the scheme open pom.xml and change in configuration generator. 
Set database and login information in pom.xml and src/main/java/DataBase/DatasourceConfig. You need a HERE developer Account to get an API Key. This is needed to 
request traffic incident data from the provider HERE.

### Requirements

* HERE Account to request Traffic API
* PostgreSQL Database with PostGIS extension 
* Routable OSM road topology 
* scheme openlr in your map database 

### Build

    mvn clean install

## Resources

* TomTom OpenLR implementation: <https://github.com/tomtom-international/openlr>
* HERE Traffic API: <https://developer.here.com/documentation/traffic/dev_guide/topics/incident-data.html>
* PostgreSQL: <https://www.postgresql.org>
* PostGIS: <https://postgis.net>
* pgRouting: <https://pgrouting.org>
*jOOQ: <https://www.jooq.org>

## License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

