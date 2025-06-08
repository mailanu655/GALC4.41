# Directory Tree: ahm_mfg_galc-release-4.41\aggregator\galc-client-fx\target\resource

Copy

Paste

```
resource/
├── com/
│   └── honda/
│       ├── galc/
│       │   ├── client/
│       │   │   ├── dc/
│       │   │   ├── fx/
│       │   │   │   └── widget/
│       │   │   │       └── ProcessCycleTimerWidget.fxml
│       │   │   ├── images/
│       │   │   │   └── qi/
│       │   │   ├── product/
│       │   │   ├── qics/
│       │   │   ├── qsr/
│       │   │   ├── sample/
│       │   │   ├── schedule/
│       │   │   ├── sounds/
│       │   │   ├── teamlead/
│       │   │   ├── teamleader/
│       │   │   └── ui/
│       │   ├── leclaserprotocol/
│       │   │   └── LecLaserResponses.xml
│       │   ├── message/
│       │   │   ├── MessageBundle_en_CA.properties
│       │   │   ├── MessageBundle_en_US.properties
│       │   │   ├── MessageBundle_fr_CA.properties
│       │   │   ├── MessageBundle_ja_JP.properties
│       │   │   └── MessageBundle_zh_CN.properties
│       │   └── openprotocol/
│       │       ├── OP0001.xml
│       │       ├── OP0002.xml
│       │       ├── OP0003.xml
│       │       ├── OP0004.xml
│       │       ├── OP0005.xml
│       │       ├── OP0014.xml
│       │       ├── OP0015.xml
│       │       ├── OP0016.xml
│       │       ├── OP0018.xml
│       │       ├── OP0036.xml
│       │       ├── OP0038.xml
│       │       ├── OP0042.xml
│       │       ├── OP0043.xml
│       │       ├── OP0050.xml
│       │       ├── OP0060.xml
│       │       ├── OP0061.xml
│       │       ├── OP0062.xml
│       │       ├── OP0080.xml
│       │       ├── OP0081.xml
│       │       ├── OP0082.xml
│       │       ├── OP0100.xml
│       │       ├── OP0101.xml
│       │       ├── OP0101SpindleStatus.xml
│       │       ├── OP0102.xml
│       │       ├── OP0105.xml
│       │       ├── OP0106.xml
│       │       ├── OP0108.xml
│       │       ├── OP0127.xml
│       │       ├── OP9999.xml
│       │       ├── OPCommandErrors.xml
│       │       └── OPHeader.xml
│       └── global/
│           └── galc/
│               └── client/
│                   ├── immobiprotocol/
│                   │   ├── ABORT.xml
│                   │   ├── ERR.xml
│                   │   ├── KEY.xml
│                   │   ├── KEY_ACK.xml
│                   │   ├── KEY_NG.xml
│                   │   ├── KEY_OK.xml
│                   │   ├── MTOC.xml
│                   │   ├── MTOC_ACK.xml
│                   │   ├── MTOC_NG.xml
│                   │   ├── MTOC_OK.xml
│                   │   ├── REG_DONE.xml
│                   │   ├── REG_NG.xml
│                   │   ├── REG_OK.xml
│                   │   ├── VIN.xml
│                   │   ├── VIN_ACK.xml
│                   │   └── VIN_NG.xml
│                   └── qics/
│                       └── view/
├── css/
│   ├── 2SDdefault.css
│   ├── default.css
│   ├── demo.css
│   ├── mfg-ctrl-maint-screen.css
│   ├── QiMainCss.css
│   ├── QiTouchScreenCss.css
│   ├── structure-delete.css
│   ├── TableViewRowHighlighting.css
│   └── TeamLeader.css
├── images/
│   ├── bbscales/
│   ├── common/
│   └── immobi/
├── let_printer_fop_templates/
├── schema/
│   ├── QiStationConfig.xsd
│   └── QMtcModelImportExport.xsd
└── template/
    ├── ireport/
    │   └── NAQ/
    └── ProductChecker.properties
```

## Directory Structure Overview

This resource directory appears to be part of a client application for a system called GALC (Global Assembly Line Control). The structure contains:

1. **Configuration Files**: XML files for protocols, XSD schema files, and property files
2. **UI Resources**: CSS stylesheets, images, and FXML files for JavaFX interfaces
3. **Internationalization**: Message bundles for multiple languages (English, French, Japanese, Chinese)
4. **Templates**: Report templates and printer templates
5. **Protocol Definitions**: XML files defining communication protocols (openprotocol, immobiprotocol, leclaserprotocol)

The directory is organized in a hierarchical manner with clear separation between different types of resources, which is typical for a Java/JavaFX application following standard package naming conventions