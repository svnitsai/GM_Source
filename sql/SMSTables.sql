USE [ProdPayC]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[SMSContactHistory](
	[HistoryId] [int] IDENTITY(1,1) NOT NULL,
	[RequestInitiatedTS] [varchar] (25) NOT NULL, --Serves as a grouping
	[RequestVendorReferrence] [varchar] (50) NULL, -- Messageid in case of Apex
	[PayCReferenceNumber] [numeric] (11,0) NOT NULL,
	[CustId] [numeric](11, 0)  NOT NULL,
	[CustCode] [numeric](9, 0) NOT NULL,
	[CustName] [varchar](50) NOT NULL,
	[SMSMobileNumber] [numeric](16, 0) NOT NULL,
	[SMSMobileOwnerName] [varchar](50) NOT NULL,
	[SMSMessage] [varchar] (200) NOT NULL,
	[SMSVendor] [varchar] (50) NOT NULL,
	[SentTimeStamp] [datetime] NULL,
	[FailedReason] [varchar] (200) NULL,
	[Status] [varchar] (30) NOT NULL, --Initial, Sent, failed
	[StatusUpdatedTS] [datetime] NOT NULL,
 CONSTRAINT [PK_SMSContactHistory] PRIMARY KEY CLUSTERED 
(
	[HistoryId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
) ON [PRIMARY]
GO

SET ANSI_PADDING OFF
GO
-----------------------------------------
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[SMSTemplate](
	[TemplateId] [int] IDENTITY(1,1) NOT NULL,
	[TemplateName] [varchar] (50) NOT NULL,
	[Template] [varchar] (200) NOT NULL,
	[CreatedDate] [datetime] NOT NULL,
	[CreatedBy] [varchar] (50) NOT NULL,
	[UpdatedDate] [datetime] NOT NULL,
	[UpdatedBy] [varchar] (50) NOT NULL,
 CONSTRAINT [PK_SMSTemplate] PRIMARY KEY CLUSTERED 
(
	[TemplateId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
) ON [PRIMARY]
GO

SET ANSI_PADDING OFF
GO
----------------------------------------
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[SMSRequestResponse](
	[RequestResponseId] [int] IDENTITY(1,1) NOT NULL,
	[RequestInitiatedTS] [varchar] (25) NOT NULL, --Connector to History table
	[Request] [varchar] (800) NOT NULL,
	[Response] [varchar] (800) NOT NULL,
 CONSTRAINT [PK_SMSRequestResponse] PRIMARY KEY CLUSTERED 
(
	[RequestResponseId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

