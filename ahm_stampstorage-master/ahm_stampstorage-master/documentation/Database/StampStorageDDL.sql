/****** Object:  Table [dbo].[StampingStatus]    Script Date: 02/10/2011 13:58:08 ******/
CREATE TABLE [dbo].[StampingStatus](
	[StampingStatus_ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[CarrierNumber] [numeric](18, 0) NOT NULL,
	[CurrentDestination] [varchar](10) NULL,
	[RequestedDestination] [varchar](10) NULL,
	[InTimestamp] [datetime] NULL,
	[OutTimestamp] [datetime] NULL,
 CONSTRAINT [PK_StampingStatus] PRIMARY KEY CLUSTERED 
(
	[StampingStatus_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

/****** Object:  Table [dbo].[StampingStatusAudit]    Script Date: 02/10/2011 13:58:13 ******/
CREATE TABLE [dbo].[StampingStatusAudit](
	[StampingStatusAudit_ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[CarrierNumber] [varchar](50) NOT NULL,
	[CurrentDestination] [varchar](50) NULL,
	[RequestedDestination] [varchar](50) NULL,
	[InTimestamp] [datetime] NULL,
	[OutTimestamp] [datetime] NULL,
 CONSTRAINT [PK_StampingStatusAudit] PRIMARY KEY CLUSTERED 
(
	[StampingStatusAudit_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

/****** Object:  Table [dbo].[CarrierInfo]    Script Date: 02/10/2011 13:57:48 ******/
CREATE TABLE [dbo].[CarrierInfo](
	[CarrierInfo_ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[CarrierNumber] [numeric](18, 0) NOT NULL,
	[DieNumber] [numeric](18, 0) NULL,
	[Quantity] [int] NULL,
	[Model] [nchar](10) NULL,
	[Type] [nchar](10) NULL,
	[InTimestamp] [datetime] NULL,
	[OutTimestamp] [datetime] NULL,
 CONSTRAINT [PK_CarrierInfo] PRIMARY KEY CLUSTERED 
(
	[CarrierInfo_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

/****** Object:  Table [dbo].[CarrierInfoAudit]    Script Date: 02/10/2011 14:04:00 ******/
CREATE TABLE [dbo].[CarrierInfoAudit](
	[CarrierInfoAudit_ID] [numeric](18, 0) NOT NULL,
	[CarrierNumber] [numeric](18, 0) NOT NULL,
	[DieNumber] [numeric](18, 0) NOT NULL,
	[Quantity] [int] NULL,
	[Model] [varchar](10) NULL,
	[Type] [varchar](10) NULL,
	[InTimestamp] [datetime] NULL,
	[OutTimestamp] [datetime] NULL,
 CONSTRAINT [PK_CarrerInfoAudit] PRIMARY KEY CLUSTERED 
(
	[CarrierInfoAudit_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

/****** Object:  Table [dbo].[DescisionStops]    Script Date: 02/10/2011 13:58:03 ******/
CREATE TABLE [dbo].[DescisionStops](
	[DecisionStop_ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NOT NULL,
	[Description] [varchar](50) NULL,
 CONSTRAINT [PK_DescisionStops] PRIMARY KEY CLUSTERED 
(
	[DecisionStop_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]