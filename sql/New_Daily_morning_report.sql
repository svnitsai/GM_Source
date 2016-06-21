SELECT distinct DC.PayCDueDate, CONVERT(varchar,DC.PayCDueDate,112),DC.PayCStatus, C.CustName, C.CustCity, 
DC.InvoiceReferenceNumber, C.CustContactNumber, DC.InvoiceAmount, DC.DeferredDate, 
DC.PayCReferenceNumber, DC.CustCode, DC.InvoiceDate, DC.DeferredReason, DC.Remarks,
CONVERT(varchar,DC.DeferredDate,112) as converted,
(select SUM(ISNULL(DCDD.PaidAmount, 0)) From DailyPayCDetails DCDD 
	WHERE DCDD.PayCReferenceNumber=DCD.PayCReferenceNumber and DCD.Deleted <> 1
) AS TotalPaidAmount
FROM DailyPayC DC
join Customer c on c.custcode = dc.custcode and c.custtype = 'Merchant'
join PartyContactDetails PCD on PCD.PTYCODE = c.custcode
left join DailyPayCDetails DCD ON DC.PayCReferenceNumber = DCD.PayCReferenceNumber AND DCD.Deleted <> 1
left join Customer s on s.custcode = dcd.suppliercode and s.custtype = 'Supplier'
left join CustomerBanks CB1 
	on CB1.CustBankId = DCD.SupplierBankId and s.custcode = cb1.custcode
WHERE 
DC.PayCStatus <> 'CLOSED'
ORDER BY PayCDueDate, CustCode
OR
CONVERT(varchar,DC.DeferredDate,112) = CONVERT(varchar, '05-02-2016', 112)
(CONVERT(varchar,DC.PayCDueDate,112) = CONVERT(varchar, '03-May-2016', 112)
)
AND
