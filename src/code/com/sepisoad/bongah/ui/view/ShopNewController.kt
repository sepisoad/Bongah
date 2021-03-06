package com.sepisoad.bongah.ui.view

import com.sepisoad.bongah.app.*
import com.sepisoad.bongah.db.TableShop
import com.sepisoad.bongah.model.*
import com.sepisoad.bongah.ui.*
import com.sepisoad.bongah.ui.form.ShopFormPrinter
import com.sepisoad.bongah.ui.helper.*
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.NodeOrientation
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import jodd.io.FileNameUtil
import java.io.File
import java.net.URI
import java.text.DecimalFormat
import java.util.*

class ShopNewController {
  var parent: Stage? = null
  var movedPictures: MutableList<URI> = mutableListOf()
  var tempPictures: MutableList<URI> = mutableListOf()
  var viewMode = ViewMode.STAND_ALONE
  var editingObjId = 0
  lateinit var onAfterSaveAction: OnAfterSaveAction
  lateinit var updateActionHandler: UpdateActionHandler
  lateinit var textFieldDate: TextField
  @FXML lateinit var labelDocumentDate: Label;
  @FXML lateinit var labelReferrer: Label;
  @FXML lateinit var textFieldReferrer: TextField;
  @FXML lateinit var buttonFindReferrerId: Button;

  @FXML private lateinit var labelOwnerName: Label
  @FXML private lateinit var labelPhoneNumbers: Label
  @FXML private lateinit var labelAddress: Label
  @FXML private lateinit var labelSindh: Label
  @FXML private lateinit var textFieldPrice: TextField
  @FXML private lateinit var comboBoxSindhType: ComboBox<SindhType>
  @FXML private lateinit var labelPrice: Label
  @FXML private lateinit var buttonPictures: Button
  @FXML private lateinit var textFieldArea: TextField
  @FXML private lateinit var textFieldAddress: TextField
  @FXML private lateinit var checkBoxIsSwappable: CheckBox
  @FXML private lateinit var textFieldOwnerName: TextField
  @FXML private lateinit var labelArea: Label
  @FXML private lateinit var textFieldPhoneNumbers: TextField
  @FXML private lateinit var checkBoxHasWater: CheckBox
  @FXML private lateinit var checkBoxHasElectricity: CheckBox
  @FXML private lateinit var checkBoxHasGas: CheckBox
  @FXML private lateinit var checkBoxHasPhone: CheckBox
  @FXML private lateinit var labelFacilityPhoneNumbers: Label
  @FXML private lateinit var textFieldFacilityPhoneNumbers: TextField
  @FXML private lateinit var buttonSave: Button
  @FXML private lateinit var buttonPrintPreview: Button

  @FXML private lateinit var comboBoxOwnershipType: ComboBox<OwnershipType>
  @FXML private lateinit var comboBoxRoofType: ComboBox<RoofType>
  @FXML private lateinit var labelOwnershipType: Label
  @FXML private lateinit var labelRoofType: Label

  @FXML private lateinit var textAreaDescription: TextArea

  @FXML private fun initialize(): Unit {
    textFieldDate.textProperty().addListener(DateChangeListener(textFieldDate))
    textFieldReferrer.textProperty().addListener(IntegerNumericChangeListener(textFieldReferrer))
    textFieldAddress.textProperty().addListener(SqlChangeListener(textFieldAddress))
    textFieldOwnerName.textProperty().addListener(SqlChangeListener(textFieldOwnerName))
    textFieldPhoneNumbers.textProperty().addListener(SqlChangeListener(textFieldPhoneNumbers))
    textFieldFacilityPhoneNumbers.textProperty().addListener(SqlChangeListener(textFieldFacilityPhoneNumbers))
    textFieldPrice.textProperty().addListener(FloatingNumericChangeListener(textFieldPrice))
    textFieldArea.textProperty().addListener(FloatingNumericChangeListener(textFieldArea))

    labelDocumentDate.text = PROPS.getProperty("documentDate")
    labelReferrer.text = PROPS.getProperty("referrerCode")
    buttonFindReferrerId.text = PROPS.getProperty("findReferrerId")
    labelOwnerName.text = PROPS.getProperty("owner")
    labelPhoneNumbers.text = PROPS.getProperty("phoneNumbers")
    labelAddress.text = PROPS.getProperty("address")
    labelSindh.text = PROPS.getProperty("sindhType")
    labelPrice.text = PROPS.getProperty("price")
    buttonPictures.text = PROPS.getProperty("pictures")
    checkBoxIsSwappable.text = PROPS.getProperty("isSwappable")
    labelArea.text = PROPS.getProperty("area")
    checkBoxHasWater.text = PROPS.getProperty("water")
    checkBoxHasElectricity.text = PROPS.getProperty("electricity")
    checkBoxHasGas.text = PROPS.getProperty("gas")
    checkBoxHasPhone.text = PROPS.getProperty("phone")
    labelFacilityPhoneNumbers.text = PROPS.getProperty("registeredPhoneNumbers")
    labelRoofType.text = PROPS.getProperty("roofType")
    labelOwnershipType.text = PROPS.getProperty("ownershipType")
    buttonSave.text = PROPS.getProperty("save")
    buttonPrintPreview.text = PROPS.getProperty("printPreview")

    comboBoxSindhType.items?.add(SindhType.UNDEFINED)
    comboBoxSindhType.items?.add(SindhType.ENDOWMENT)
    comboBoxSindhType.items?.add(SindhType.URBAN)
    comboBoxSindhType.selectionModel?.select(0)
    comboBoxOwnershipType.items.add(OwnershipType.UNDEFINED)
    comboBoxOwnershipType.items.add(OwnershipType.ARENA)
    comboBoxOwnershipType.items.add(OwnershipType.COMPLETE)
    comboBoxOwnershipType.selectionModel.select(0)
    comboBoxRoofType.items.add(RoofType.UNDEFINED)
    comboBoxRoofType.items.add(RoofType.FREE)
    comboBoxRoofType.items.add(RoofType.UNDER_BUILDING)
    comboBoxRoofType.items.add(RoofType.INSIDE_BUILDING)
    comboBoxRoofType.selectionModel.select(0)
  }

  fun getShopInfo(): ShopInfo {
    val referrerId = if(!textFieldReferrer.text.isNullOrEmpty())
      textFieldReferrer.text.toInt() else -1
    val hasReferrer = if (referrerId != -1) true else false
    val date = textFieldDate.text

    val address = textFieldAddress.text
    val ownerName = textFieldOwnerName.text
    val ownerPhoneNumbers = textFieldPhoneNumbers.text
    val sindhType = comboBoxSindhType.selectionModel.selectedItem
    val isSwappable = checkBoxIsSwappable.isSelected
    val phoneNumbers = textFieldFacilityPhoneNumbers.text
    val hasPhone = checkBoxHasPhone.isSelected
    val hasWater = checkBoxHasWater.isSelected
    val hasElectricity = checkBoxHasElectricity.isSelected
    val hasGas = checkBoxHasGas.isSelected
    val latitude = 0.0 //FIXME
    val longitude = 0.0 //FIXME
    val pictures = ""
    val description = textAreaDescription.text

    val ownershipType = comboBoxOwnershipType.selectionModel.selectedItem
    val roofType = comboBoxRoofType.selectionModel.selectedItem

    var area = 0.0
    var price = 0.0

    try{
      area = if(!textFieldArea.text.isNullOrEmpty())
        textFieldArea.text.toDouble() else 0.0
    }catch(ex: NumberFormatException){
      throw AppBaseException(PROPS.getProperty("alertMsgInvalidAreaValue"))
    }

    try{
      price = if(!textFieldPrice.text.isNullOrEmpty())
        textFieldPrice.text.toDouble() else 0.0
    }catch(ex: NumberFormatException){
      throw AppBaseException(PROPS.getProperty("alertMsgInvalidPriceValue"))
    }

    if(ownerName.isNullOrEmpty()) {
      throw AppBaseException(PROPS.getProperty("alertMsgOwnerNotSet"))
    }
    if(address.isNullOrEmpty()) {
      throw AppBaseException(PROPS.getProperty("alertMsgAddressNotSet"))
    }

    val obj = ShopInfo(
      0, hasReferrer, referrerId, date.toString(),
      address, area, ownerName, ownerPhoneNumbers, sindhType, price, isSwappable, phoneNumbers, hasPhone, hasWater,
      hasElectricity, hasGas, latitude, longitude, pictures, description,
      ownershipType, roofType)
    return obj
  }

  fun populateData(obj: ShopInfo): Unit {
    val decimalFormat = DecimalFormat("#.##")
    textFieldDate.text = obj.date;
    textFieldReferrer.text = obj.referrerId.toString()
    textFieldPrice.text = decimalFormat.format(obj.price)
    textFieldArea.text = decimalFormat.format(obj.area)
    textFieldAddress.text = obj.address
    textFieldOwnerName.text = obj.ownerName
    textFieldPhoneNumbers.text = obj.ownerPhoneNumbers
    textFieldFacilityPhoneNumbers.text = obj.phoneNumbers

    checkBoxIsSwappable.isSelected = obj.isSwappable
    checkBoxHasWater.isSelected = obj.hasWater
    checkBoxHasElectricity.isSelected = obj.hasElectricity
    checkBoxHasGas.isSelected = obj.hasGas
    checkBoxHasPhone.isSelected = obj.hasPhone

    comboBoxSindhType.selectionModel.select(obj.sindhType)
    comboBoxOwnershipType.selectionModel.select(obj.ownershipType)
    comboBoxRoofType.selectionModel.select(obj.roofType)

    textAreaDescription.text = obj.description

    getPicturesPath(obj)
    editingObjId = obj.id
  }

  @FXML fun onButtonSaveAction(): Unit {
    do{
      var whenBlockSuccessful = false
      when(viewMode){
        ViewMode.STAND_ALONE -> {
          try{
            val shopInfo = getShopInfo()
            val id = TableShop.insert(shopInfo)
            if(id != -1){
              if(movePictures(id)){
                val pictures = getMovedFileName()
                if(TableShop.updatePictures(pictures, id)){
                  val alertMsg = PROPS.getProperty("alertMsgSavedSuccessfully")
                  val alert = Alert(Alert.AlertType.INFORMATION, alertMsg, ButtonType.CLOSE)
                  alert.showAndWait()
                  onAfterSaveAction.handle()
                  whenBlockSuccessful = true
                }
              }
            }
          }catch(ex: AppBaseException){
            val alert = Alert(Alert.AlertType.WARNING, ex.message, ButtonType.CLOSE)
            alert.showAndWait()
          }
        }
        ViewMode.DEPENDENT -> {
          try{
            val shopInfo = getShopInfo()
            movePictures(editingObjId)
            shopInfo.pictures = getMovedFileName()
            if(updateActionHandler.handle(shopInfo)) {
              val alertMsg = PROPS.getProperty("alertMsgUpdatedSuccessfully")
              val alert = Alert(Alert.AlertType.INFORMATION, alertMsg, ButtonType.CLOSE)
              alert.showAndWait()
              onAfterSaveAction.handle()
              whenBlockSuccessful = true
            }else{
              //TODO
            }

            if(null != parent){
              parent?.hide()
            }
          }catch(ex: AppBaseException){
            val alert = Alert(Alert.AlertType.WARNING, ex.message, ButtonType.CLOSE)
            alert.showAndWait()
          }
        }
      }
      if(!whenBlockSuccessful)
        break
    }while(false)
  }

  @FXML fun onButtonPrintPreviewAction(): Unit{
    try{
      val orientation = if(Config.layoutDirection == "LFT")
        NodeOrientation.LEFT_TO_RIGHT else NodeOrientation.RIGHT_TO_LEFT

      val shopInfo = getShopInfo()
      ShopFormPrinter.print(FxApp.appObj, orientation, shopInfo)
    }catch(ex: AppBaseException){
      val alert = Alert(Alert.AlertType.WARNING, ex.message, ButtonType.CLOSE)
      alert.showAndWait()
    }
  }

  @FXML fun onButtonFindReferrerIdAction(): Unit{
    val loader = FXMLLoader()

    val cl = FxApp::class.java.classLoader
    loader.location = cl.getResource("res/ui/colleague_find.fxml")

    val root = loader.load<VBox>()
    var colleagueFindCtrl: ColleagueFindController
    colleagueFindCtrl = loader.getController()
    colleagueFindCtrl.findReferrerIdActionHandler = FindReferrerId()
    colleagueFindCtrl.viewMode = ViewMode.DEPENDENT

    val stage = Stage()
    val scene = Scene(root)
    stage.scene = scene

    if(Config.layoutDirection.equals("RTL"))
      root.nodeOrientation = NodeOrientation.RIGHT_TO_LEFT
    if(Config.layoutDirection.equals("LTR"))
      root.nodeOrientation = NodeOrientation.LEFT_TO_RIGHT

    root.setPrefSize(500.0, 300.0)
    stage.initModality(Modality.APPLICATION_MODAL)
    stage.showAndWait()
    colleagueFindCtrl.findReferrerIdAction()
  }

  @FXML fun onButtonPicturesAction(/*event: ActionEvent*/): Unit {
    val loader = FXMLLoader()

    val cl = FxApp::class.java.classLoader
    loader.location = cl.getResource("res/ui/pictures.fxml")

    val root = loader.load<VBox>()
    var picturesCtrl: PicturesController
    picturesCtrl = loader.getController()

    val stage = Stage()
    val scene = Scene(root)
    stage.scene = scene

    stage.onCloseRequest = EventHandler {
      event ->
      tempPictures = picturesCtrl.tempPictures
      movedPictures = picturesCtrl.movedPictures
    }

    if(Config.layoutDirection.equals("RTL"))
      root.nodeOrientation = NodeOrientation.RIGHT_TO_LEFT
    if(Config.layoutDirection.equals("LTR"))
      root.nodeOrientation = NodeOrientation.LEFT_TO_RIGHT

    scene.stylesheets.add(cl.getResource("res/css/base.css").toExternalForm())

    stage.initModality(Modality.APPLICATION_MODAL)
    picturesCtrl.setPictures(tempPictures, movedPictures)
    stage.showAndWait()
  }

  fun movePictures(id: Int): Boolean{
    var result = false

    do{
      val imageDir = File(APP_SHOP_IMG_DIR_PATH + File.separator + id.toString())
      if(tempPictures.size > 0)
        if(!imageDir.exists())
          if(!imageDir.mkdir())
            break
      for(pic in tempPictures){
        val uuid = UUID.randomUUID()
        val targetFile = File(APP_SHOP_IMG_DIR_PATH +
          File.separator +
          id.toString() +
          File.separator +
          uuid.toString() + "." + FileNameUtil.getExtension(pic.toASCIIString()))
        val sourceFile = File(pic)
        sourceFile.copyTo(targetFile, true)
        movedPictures.add(targetFile.toURI())
      }
      tempPictures.clear()
      result = true
    }while(false)

    return result
  }

  fun getPicturesFileName(): String {
    var result = ""

    for(file in tempPictures)
    {
      result += FileNameUtil.getName(file.toString()) + "#"
    }

    for(file in movedPictures)
    {
      result += FileNameUtil.getName(file.toString()) + "#"
    }

    return result
  }

  fun getMovedFileName(): String {
    var result = ""

    for(file in movedPictures)
    {
      result += FileNameUtil.getName(file.toString()) + "#"
    }

    return result
  }

  fun getPicturesPath(obj: ShopInfo): Unit{
    val listNames = obj.pictures.split('#')
    if(listNames.size <= 0)
      return

    val imageDirPath = APP_SHOP_IMG_DIR_PATH + File.separator + obj.id.toString()
    for(name in listNames){
      if(name == "")
        continue
      val imageFilePath = File(imageDirPath + File.separator + name)
      movedPictures.add(imageFilePath.toURI())
    }
  }

  //--------------------------
  inner class FindReferrerId : FindReferrerIdActionHandler {
    override fun handle(OBJ: Any?) {
      if(null != OBJ){
        val obj = OBJ as ColleagueInfo
        textFieldReferrer.text = obj.id.toString()
      }
    }
  }
}
