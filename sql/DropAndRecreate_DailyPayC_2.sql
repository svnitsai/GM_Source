USE [PayC]
GO

/****** Object:  Table [dbo].[DailyPayC]    Script Date: 12/30/2014 19:00:59 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO
  
  
CREATE TABLE [dbo].[DailyPayCNew](
	[PayCReferenceNumber] [int] IDENTITY(1,1) NOT NULL,
	[PayCDueDate] [datetime] NOT NULL,
	[CustCode] [numeric](9, 0) NOT NULL,
	[InvoiceAmount] [numeric](14, 2) NULL,
	[PayCStatus] [varchar](20) NULL,
	[InvoiceReferenceNumber] [numeric](9, 0) NOT NULL,
	[InvoiceDate] [datetime] NULL,
	[DeferredDate] [datetime] NULL,
	[CreatedDate] [datetime] NOT NULL,
	[CreatedBy] [varchar](50) NOT NULL,
	[UpdatedDate] [datetime] NOT NULL,
	[UpdatedBy] [varchar](50) NOT NULL,
 CONSTRAINT [PK_DailyPayCNew] PRIMARY KEY CLUSTERED 
(
	[PayCReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_DailyPayCNew] UNIQUE NONCLUSTERED 
(
	[PayCDueDate] ASC,
	[InvoiceReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

INSERT INTO [PayC].[dbo].[DailyPayCNew]
           ([PayCDueDate]
           ,[CustCode]
           ,[InvoiceAmount]
           ,[PayCStatus]
           ,[InvoiceReferenceNumber]
           ,[DeferredDate]
           ,[CreatedDate]
           ,[CreatedBy]
           ,[UpdatedDate]
           ,[UpdatedBy])
SELECT [PayCDueDate]
      ,[CustCode]
      ,[InvoiceAmount]
      ,[PayCStatus]
      ,[InvoiceReferenceNumber]
      ,[DeferredDate]
      ,[CreatedDate]
      ,[CreatedBy]
      ,[UpdatedDate]
      ,[UpdatedBy]
  FROM [PayC].[dbo].[DailyPayC1]


SET ANSI_PADDING OFF
GO
