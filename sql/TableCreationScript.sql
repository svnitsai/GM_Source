USE [Local1]
GO
/****** Object:  Table [dbo].[DailyCollection]    Script Date: 11/07/2014 15:35:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DailyCollection](
	[CollectionRefereneNumber] [int] IDENTITY(1,1) NOT NULL,
	[CollectionDueDate] [datetime] NULL,
	[CollectionStatus] [varchar](15) NULL,
	[InvoiceReferenceNumber] [numeric](9, 0) NOT NULL,
 CONSTRAINT [PK_DailyCollection] PRIMARY KEY CLUSTERED 
(
	[CollectionRefereneNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_DailyCollection] UNIQUE NONCLUSTERED 
(
	[CollectionDueDate] ASC,
	[InvoiceReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF

USE [Local1]
GO
/****** Object:  Table [dbo].[OriginalBill]    Script Date: 11/07/2014 16:10:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[OriginalBill](
	[InvoiceReferenceNumber] [int] IDENTITY(1,1) NOT NULL,
	[PartyId] [numeric](9, 0) NULL,
	[PartyName] [varchar](50) NULL,
	[PartyContactNumber] [numeric](16, 0) NULL,
	[InvoiceAmount] [numeric](10, 2) NULL,
	[CollectionDueDate] [datetime] NULL
 CONSTRAINT [PK_OriginalBill] PRIMARY KEY CLUSTERED 
(
	[InvoiceReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UQ_OriginalBill] UNIQUE NONCLUSTERED 
(
	[PartyId] ASC,
	[InvoiceReferenceNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF

USE [Local1]
GO
/****** Object:  Table [dbo].[DailyPayable]    Script Date: 11/07/2014 16:35:47 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DailyPayable](
	[PayableReferenceNumber] [int] IDENTITY(1,1) NOT NULL,
	[PayableDate] [datetime] NULL,
	[PayableAmount] [numeric](10, 2) NULL,
	[SupplierId] [numeric](9, 0) NULL,
	[SupplierAccountNumber] [varchar](50) NULL
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

USE [Local1]
GO
/****** Object:  Table [dbo].[DailyPayable]    Script Date: 11/07/2014 16:35:47 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DailyPayable](
	[PayableReferenceNumber] [int] IDENTITY(1,1) NOT NULL,
	[PayableDate] [datetime] NULL,
	[PayableAmount] [numeric](10, 2) NULL,
	[SupplierId] [numeric](9, 0) NULL,
	[SupplierAccountNumber] [varchar](50) NULL
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

USE [Local1]
GO
/****** Object:  Table [dbo].[SupplierDetails]    Script Date: 11/07/2014 16:10:29 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SupplierDetails](
	[SupplierId] [numeric](9, 0) NOT NULL,
	[SupplierName] [varchar](50) NULL,
	[SupplierContactNumber] [numeric](16, 0) NULL,
	[SupplierAccountNumber] [varchar](50) NOT NULL
 CONSTRAINT [PK_SupplierDetails] PRIMARY KEY CLUSTERED 
(
	[SupplierId] ASC,
	[SupplierAccountNumber] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
)
GO
SET ANSI_PADDING OFF
