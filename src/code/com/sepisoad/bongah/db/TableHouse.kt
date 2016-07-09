package com.sepisoad.bongah.db

import com.sepisoad.bongah.model.*
import java.sql.SQLException
import kotlin.reflect.jvm.internal.impl.com.google.protobuf.LazyStringArrayList

object TableHouse{
  final val TABLE_NAME = "HOUSE"

  fun insert(obj: HouseInfo): Int {
    var result = -1
    val c = DbManager.getConnection()
    val query =
      """
INSERT INTO ${TABLE_NAME}
(HASREFERRER, REFERRERID, DATE, ADDRESS, AREA, OWNERNAME, OWNERPHONENUMBERS, SINDHTYPE, PRICE, ISSWAPPABLE,
 PHONENUMBERS, HASPHONE, HASWATER, HASELECTRICITY, HASGAS, LATITUDE, LONGITUDE, PICTURES, HOUSETYPE,
 TERRAINTYPE, DESCRIPTION, BUILDINGLEVELSCOUNT, LEVELNUMBER, BEDROOMSCOUNT, HASSEPARATEWAY, HASFURNITURE,
 HASSTORAGEROOM, HASPARKING, HASVIDEODOORPHONE, HASAIRCONDITIONER, HASPACKAGEPLUMBING, HASCABINET,
 HASWALLDRESSER, HASLOAN, LOANAMOUNT) VALUES
('${obj.hasReferrer}', '${obj.referrerId}', '${obj.date}',
 '${obj.address}', '${obj.area}', '${obj.ownerName}',
 '${obj.ownerPhoneNumbers}', '${obj.sindhType}', '${obj.price}',
 '${obj.isSwappable}', '${obj.phoneNumbers}', '${obj.hasPhone}',
 '${obj.hasWater}', '${obj.hasElectricity}', '${obj.hasGas}',
 '${obj.latitude}', '${obj.longitude}', '${obj.pictures}',
 '${obj.houseType}', '${obj.terrainType}', '${obj.description}',
 '${obj.buildingLevelsCount}', '${obj.levelNumber}', '${obj.bedRoomsCount}', '${obj.hasSeparateWay}',
 '${obj.hasFurniture}', '${obj.hasStorageRoom}', '${obj.hasParking}', '${obj.hasVideoDoorPhone}',
 '${obj.hasAirConditioner}', '${obj.hasPackagePlumbing}', '${obj.hasCabinet}', '${obj.hasWallDresser}',
 '${obj.hasLoan}', '${obj.loanAmount}');
"""
    do{
      try {
        val sttmnt = c.createStatement()
        sttmnt.executeUpdate(query)
        val rs = sttmnt.executeQuery("SELECT ID from ${TABLE_NAME} order by ID DESC limit 1")
        rs.next()
        result = rs.getInt("ID")
        sttmnt.closeOnCompletion()
      }catch(ex: SQLException) {
        ex.printStackTrace() //TODO
        break
      }

    }while(false)

    DbManager.disConnect(c)
    return result
  }

  fun find(houseInfo: HouseInfo, priceFrom: String?, priceTo: String?, areaFrom: String?, areaTo: String?)
    : MutableList<HouseInfo>
  {
    var result: MutableList<HouseInfo> = mutableListOf()

    val connection = DbManager.getConnection()
    val isSwappable = " ISSWAPPABLE = '${houseInfo.isSwappable}' "
    val hasLoan = " HASLOAN = '${houseInfo.hasLoan}' "
    val houseType = " HOUSETYPE = '${houseInfo.houseType}' "
    val terrainType = " TERRAINTYPE = '${houseInfo.terrainType}' "
    val sindhType = " SINDHTYPE = '${houseInfo.sindhType}' "
    val address = " ADDRESS LIKE '%${houseInfo.address}%' "
    val owner = " OWNERNAME LIKE '%${houseInfo.ownerName}%' "

    var price = ""
    if(!priceFrom.isNullOrEmpty() and !priceTo.isNullOrEmpty())
      price = " PRICE BETWEEN ${priceFrom} AND ${priceTo} "
    if(!priceFrom.isNullOrEmpty() and priceTo.isNullOrEmpty())
      price = " PRICE > ${priceFrom} "
    if(priceFrom.isNullOrEmpty() and !priceTo.isNullOrEmpty())
      price = " PRICE < ${priceTo} "

    var area = ""
    if(!areaFrom.isNullOrEmpty() and !areaTo.isNullOrEmpty())
      area = " AREA BETWEEN ${areaFrom} AND ${areaTo} "
    if(!areaFrom.isNullOrEmpty() and areaTo.isNullOrEmpty())
      area = " AREA > ${areaFrom} "
    if(areaFrom.isNullOrEmpty() and !areaTo.isNullOrEmpty())
      area = " AREA < ${areaTo} "

    val clauseArr = LazyStringArrayList()
    if(houseInfo.isSwappable)
      clauseArr.add(isSwappable)
    if(houseInfo.hasLoan)
      clauseArr.add(hasLoan)
    if(!price.isBlank())
      clauseArr.add(price)
    if(!area.isBlank())
      clauseArr.add(area)
    if(houseInfo.houseType != HouseType.UNDEFINED)
      clauseArr.add(houseType)
    if(houseInfo.terrainType != TerrainType.UNDEFINED)
      clauseArr.add(terrainType)
    if(houseInfo.sindhType != SindhType.UNDEFINED)
      clauseArr.add(sindhType)
    if(!houseInfo.address.isNullOrEmpty())
      clauseArr.add(address)
    if(!houseInfo.ownerName.isNullOrEmpty())
      clauseArr.add(owner)

    var query = "SELECT * FROM ${TABLE_NAME} "
    if(clauseArr.size == 1){
      query += " WHERE ${clauseArr[0]}"
    }
    else if(clauseArr.size > 1){
      query += " WHERE ${clauseArr[0]}"
      clauseArr.removeAt(0)
      for(clause in clauseArr){
        query += " AND ${clause}"
      }
    }

    do{
      try {
        val statement = connection.createStatement()
        val rs = statement.executeQuery(query)
        while(rs.next()){
          val hasReferrer = if(rs.getString("HASREFERRER") == "true") true else false
          val isSwappable = if(rs.getString("ISSWAPPABLE") == "true") true else false
          val hasWater = if(rs.getString("HASWATER") == "true") true else false
          val hasPhone = if (rs.getString("HASPHONE") == "true") true else false
          val hasElectricity = if (rs.getString("HASELECTRICITY") == "true") true else false
          val hasGas = if (rs.getString("HASGAS") == "true") true else false
          val hasSeparateway = if(rs.getString("HASSEPARATEWAY") == "true") true else false
          val hasFurniture = if(rs.getString("HASFURNITURE") == "true") true else false
          val hasStorageRoom = if(rs.getString("HASSTORAGEROOM") == "true") true else false
          val hasParking = if(rs.getString("HASPARKING") == "true") true else false
          val hasVideoDoorPhone = if(rs.getString("HASVIDEODOORPHONE") == "true") true else false
          val hasAirConditioner = if(rs.getString("HASAIRCONDITIONER") == "true") true else false
          val hasPackagePlumbing = if(rs.getString("HASPACKAGEPLUMBING") == "true") true else false
          val hasCabinet = if(rs.getString("HASCABINET") == "true") true else false
          val hasWallDresser = if(rs.getString("HASWALLDRESSER") == "true") true else false
          val hasLoan = if(rs.getString("HASLOAN") == "true") true else false

          val landInfo = HouseInfo(
            rs.getInt(("ID")),
            hasReferrer,
            rs.getInt("REFERRERID"),
            rs.getString("DATE"),
            rs.getString("ADDRESS"),
            rs.getDouble("AREA"),
            rs.getString("OWNERNAME"),
            rs.getString("OWNERPHONENUMBERS"),
            SindhType.toSindhType(rs.getString("SINDHTYPE")),
            rs.getDouble("PRICE"),
            isSwappable,
            rs.getString("PHONENUMBERS"),
            hasPhone,
            hasWater,
            hasElectricity,
            hasGas,
            rs.getDouble("LATITUDE"),
            rs.getDouble("LONGITUDE"),
            rs.getString("PICTURES"),
            rs.getString("DESCRIPTION"),
            rs.getInt("BUILDINGLEVELSCOUNT"),
            rs.getInt("LEVELNUMBER"),
            rs.getInt("BEDROOMSCOUNT"),
            hasSeparateway,
            hasFurniture,
            hasStorageRoom,
            hasParking,
            hasVideoDoorPhone,
            hasAirConditioner,
            hasPackagePlumbing,
            hasCabinet,
            hasWallDresser,
            hasLoan,
            rs.getDouble("LOANAMOUNT"),
            HouseType.toHouseType(rs.getString("HOUSETYPE")),
            TerrainType.toTerrainType(rs.getString("TERRAINTYPE"))
          )
          result.add(landInfo)
        }
        statement.closeOnCompletion()
      }catch(ex: SQLException) {
        ex.printStackTrace() //TODO
        break
      }

    }while(false)

    DbManager.disConnect(connection)

    return result
  }

  fun update(obj: HouseInfo, id: Int): Boolean {
    var result = false
    val c = DbManager.getConnection()
    val query =
      """
UPDATE ${TABLE_NAME} SET
HASREFERRER = '${obj.hasReferrer}',
REFERRERID = '${obj.referrerId}',
DATE = '${obj.date}',
ADDRESS = '${obj.address}',
AREA = '${obj.area}',
OWNERNAME = '${obj.ownerName}',
OWNERPHONENUMBERS = '${obj.ownerPhoneNumbers}',
SINDHTYPE = '${obj.sindhType}',
PRICE = '${obj.price}',
ISSWAPPABLE = '${obj.isSwappable}',
PHONENUMBERS = '${obj.phoneNumbers}',
HASPHONE = '${obj.hasPhone}',
HASWATER = '${obj.hasWater}',
HASELECTRICITY = '${obj.hasElectricity}',
HASGAS = '${obj.hasGas}',
LATITUDE = '${obj.latitude}',
LONGITUDE = '${obj.longitude}',
PICTURES = '${obj.pictures}',
HOUSETYPE = '${obj.houseType}',
TERRAINTYPE = '${obj.terrainType}',
DESCRIPTION = '${obj.description}',

BUILDINGLEVELSCOUNT = '${obj.buildingLevelsCount}',
LEVELNUMBER = '${obj.levelNumber}',
BEDROOMSCOUNT = '${obj.bedRoomsCount}',
HASSEPARATEWAY = '${obj.hasSeparateWay}',
HASFURNITURE = '${obj.hasFurniture}',
HASSTORAGEROOM = '${obj.hasStorageRoom}',
HASPARKING = '${obj.hasParking}',
HASVIDEODOORPHONE = '${obj.hasVideoDoorPhone}',
HASAIRCONDITIONER = '${obj.hasAirConditioner}',
HASPACKAGEPLUMBING = '${obj.hasPackagePlumbing}',
HASCABINET = '${obj.hasCabinet}',
HASWALLDRESSER = '${obj.hasWallDresser}',
HASLOAN = '${obj.hasLoan}',
LOANAMOUNT = '${obj.loanAmount}'

WHERE ID = '${id}';
"""

    do{
      try {
        val sttmnt = c.createStatement()
        sttmnt.executeUpdate(query)
        sttmnt.closeOnCompletion()
      }catch(ex: SQLException) {
        ex.printStackTrace() //TODO
        break
      }
      result = true
    }while(false)

    DbManager.disConnect(c)
    return result
  }

  fun updatePictures(pictures: String, id: Int): Boolean {
    var result = false
    val c = DbManager.getConnection()
    val query =
      """
UPDATE ${TABLE_NAME} SET
PICTURES = '${pictures}'
WHERE ID = '${id}';
"""
    do{
      try {
        val sttmnt = c.createStatement()
        sttmnt.executeUpdate(query)
        sttmnt.closeOnCompletion()
      }catch(ex: SQLException) {
        ex.printStackTrace() //TODO
        break
      }
      result = true
    }while(false)

    DbManager.disConnect(c)
    return result
  }

  fun delete(id: Int): Boolean{
    var result = false
    val c = DbManager.getConnection()
    val query =
      """
DELETE FROM ${TABLE_NAME}
WHERE ID = '${id}';
"""

    do{
      try {
        val sttmnt = c.createStatement()
        sttmnt.executeUpdate(query)
        sttmnt.closeOnCompletion()
      }catch(ex: SQLException) {
        ex.printStackTrace()
        break
      }
      result = true
    }while(false)

    return result
  }
}
