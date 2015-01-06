USE [PayC]
GO

/****** Object:  Table [dbo].[DailyPayC]    Script Date: 12/30/2014 19:00:59 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

EXECUTE [PayC].[dbo].[CS_PayCBackup] 
GO

CREATE TABLE [dbo].[DailyPayCDetails1](
	[PayCReferenceSubNumber] [numeric](10, 0) NOT NULL,
	[PayCReferenceNumber] [numeric](10, 0) NOT NULL,
	[PayCDate] [datetime] NULL,
	[PayCType] [varchar](10) NULL,
	[SupplierCode] [numeric](9, 0) NULL,
	[SupplierBankId] [numeric](10, 0) NULL,
	[PaidAmount] [numeric](10, 2) NULL,
	[AccountLocationCode] [numeric](9, 0) NULL,
	[LedgerPageNumber] [numeric](9, 0) NULL,
	[CreatedDate] [datetime] NOT NULL,
	[CreatedBy] [varchar](50) NOT NULL,
	[UpdatedDate] [datetime] NULL,
	[UpdatedBy] [varchar](50) NULL,
	[Deleted] [int] NOT NULL,
 CONSTRAINT [PK_DailyPayCDetails1] PRIMARY KEY CLUSTERED 
(
	[PayCReferenceNumber] ASC,
	[PayCReferenceSubNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

ALTER TABLE [dbo].[DailyPayCDetails1] ADD  CONSTRAINT [DF_DailyPayCDetails1_Deleted]  DEFAULT ((0)) FOR [Deleted]
GO


INSERT INTO [PayC].[dbo].[DailyPayCDetails1]
           ([PayCReferenceSubNumber]
	       ,[PayCReferenceNumber]
           ,[PayCDate]
           ,[PayCType]
           ,[SupplierCode]
           ,[SupplierBankId]
           ,[PaidAmount]
           ,[AccountLocationCode]
           ,[LedgerPageNumber]
           ,[CreatedDate]
           ,[CreatedBy]
           ,[UpdatedDate]
           ,[UpdatedBy]
           ,[Deleted])
SELECT [PayCReferenceSubNumber]
      ,[PayCReferenceNumber]
      ,[PayCDate]
      ,[PayCType]
      ,[SupplierCode]
      ,[SupplierBankId]
      ,[PaidAmount]
      ,[AccountLocationCode]
      ,[LedgerPageNumber]
      ,[CreatedDate]
      ,[CreatedBy]
      ,[UpdatedDate]
      ,[UpdatedBy]
      ,[Deleted]
  FROM [PayC].[dbo].[DailyPayCDetails]  
GO 