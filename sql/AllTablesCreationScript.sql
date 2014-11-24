USE [PayC]
GO
/****** Object:  Table [dbo].[DailyPayC]    Script Date: 11/07/2014 15:35:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DailyPayC](
	[PayCReferenceNumber] 		[int] IDENTITY(1,1) NOT NULL,
	[PayCDueDate] 			[datetime] NOT NULL,
	[CustCode] 			[numeric](9, 0) NOT NULL,
	[InvoiceAmount] 		[numeric](14, 2) NULL,
	[PayCStatus] 			[varchar](20) NULL,
	[InvoiceReferenceNumber] 	[numeric](9, 0) NOT NULL,
	[DeferredDate] 			[datetime] NULL,
	[CreatedDate] 			[datetime] NOT NULL,
	[CreatedBy] 			[varchar] (50) NOT NULL,
	[UpdatedDate] 			[datetime] NOT NULL,
	[UpdatedBy] 			[varchar] (50) NOT NULL,
 CONSTRAINT [PK_DailyPayC] PRIMARY KEY CLUSTERED 
(
	[PayCRefereneNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_DailyPayC] UNIQUE NONCLUSTERED 
(
	[PayCDueDate] ASC,
	[InvoiceReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF

USE [PayC]
GO
/****** Object:  Table [dbo].[DailyPayCDetails]    Script Date: 11/07/2014 15:35:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DailyPayCDetails](
	[PayCReferenceNumber] 		[numeric](10,0) NOT NULL,
	[PayCReferenceSubNumber] 	[numeric](3,0) NOT NULL,
	[PayCDate] 			[datetime] NULL,
	[CustBankId] 			[numeric](10,0) NULL,
	[SupplierId] 			[numeric](10,0) NULL,
	[SupplierBankId] 		[numeric](10,0) NULL,
	[PaidAmount] 			[numeric](10, 2) NULL,
	[AccountLocationId] 		[numeric](10,0) NULL,
	[LedgerPageNumber] 		[numeric](9, 0) NULL,
	[CreatedDate] 			[datetime] NOT NULL,
	[CreatedBy] 			[varchar] (50) NOT NULL,
	[UpdatedDate] 			[datetime] NOT NULL,
	[UpdatedBy] 			[varchar] (50) NOT NULL,
 CONSTRAINT [PK_DailyPayCDetails] PRIMARY KEY CLUSTERED 
(
	[PayCRefereneNumber] ASC,
        [PayCReferenceSubNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
)

GO
SET ANSI_PADDING OFF


USE [PayC]
GO
/****** Object:  Table [dbo].[DailyPayable]    Script Date: 11/07/2014 16:35:47 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DailyPayable](
	[PayableReferenceNumber] 	[int] IDENTITY(1,1) NOT NULL,
	[PayableDate] 			[datetime] NULL,
	[PayableAmount] 		[numeric](10, 2) NULL,
	[PaidAmount]			[numeric](10,2) Null,
	[PayableStatus]			[Char] (20) Null,
	[SupplierId] 			[numeric](9, 0) NOT NULL,
	[SupplierBankId] 			[numeric](3,0) NOT NULL,
	[CreatedDate] 			[datetime] NOT NULL,
	[CreatedBy] 			[varchar] (50) NOT NULL,
	[UpdatedDate] 			[datetime] NOT NULL,
	[UpdatedBy] 			[varchar] (50) NOT NULL,
 CONSTRAINT [PK_DailyPayable] PRIMARY KEY CLUSTERED 
(
	[PayableReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_DailyPayable] UNIQUE NONCLUSTERED 
(
	[PayableDate] ASC,
	[SupplierId] ASC,
        [PayableReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF


USE [PayC]
GO
/****** Object:  Table [dbo].[Customer]    Script Date: 11/07/2014 16:10:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Customer](
	[CustId] 		[int] IDENTITY(1,10) NOT NULL,
	[CustCode] 		[numeric](9, 0) NOT NULL,
	[CustType] 		[char](20) NOT NULL,
 	[CustName] 		[varchar] (50) NOT NULL,
	[CustAddress1] 		[varchar] (50) NULL,
	[CustAddress2] 		[varchar] (50) NULL,
	[CustAddress3] 		[varchar] (50) NULL,
	[CustAddress4] 		[varchar] (50) NULL,
	[CustCity] 		[varchar] (50) NULL,
	[CustState]     	[varchar] (50) NULL,
	[CustCountry]   	[varchar] (50) NULL,
	[CustContactNumber] 	[numeric] (16,0) NULL,
	[CustExtension]		[numeric] (7,0) NULL,
	[CreatedDate] 		[datetime] NOT NULL,
	[CreatedBy] 		[varchar] (50) NOT NULL,
	[UpdatedDate] 		[datetime] NOT NULL,
	[UpdatedBy] 		[varchar] (50) NOT NULL,
 CONSTRAINT [PK_Customer] PRIMARY KEY CLUSTERED 
(
	[CustId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
 CONSTRAINT [UQ_Customer] UNIQUE NONCLUSTERED 
(
	[CustId] ASC,
	[CustCode] ASC,
        [CustType] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF

USE [PayC]
GO
/****** Object:  Table [dbo].[CustomerBanks]    Script Date: 11/07/2014 16:10:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CustomerBanks](
	[CustBankId] 		[int] IDENTITY(1,1) NOT NULL,
	[CustId] 		[numeric](9, 0) NOT NULL,
	[CustBank] 		[varchar] (50) NOT NULL,
	[CustBankBranch] 	[varchar] (50) NOT NULL,
	[CustBankAccountType] 	[Varchar] (15) NOT NULL,
	[CustBankAccountNumber] [varchar] (50) NULL,
	[CreatedDate] 		[datetime] NOT NULL,
	[CreatedBy] 		[varchar] (50) NOT NULL,
	[UpdatedDate] 		[datetime] NOT NULL,
	[UpdatedBy] 		[varchar] (50) NOT NULL,
 CONSTRAINT [PK_CustomerBanks] PRIMARY KEY CLUSTERED 
(
	[CustId] ASC,
	[CustBankId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
)
GO
SET ANSI_PADDING OFF
