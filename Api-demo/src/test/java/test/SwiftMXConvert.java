package test;

import com.prowidesoftware.swift.model.mx.AppHdr;
import com.prowidesoftware.swift.model.mx.BusinessAppHdrV02;
import com.prowidesoftware.swift.model.mx.MxPacs00900108;
import com.prowidesoftware.swift.model.mx.MxWriteConfiguration;
import com.prowidesoftware.swift.model.mx.dic.*;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class SwiftMXConvert {

    public static Party44Choice getParty44ChoiceRandom(String bicFi)
    {
        Party44Choice party44Choice = new Party44Choice();
        BranchAndFinancialInstitutionIdentification6 fiid = new BranchAndFinancialInstitutionIdentification6();
        FinancialInstitutionIdentification18 instituteID = new FinancialInstitutionIdentification18();
        instituteID.setBICFI(bicFi);
        fiid.setFinInstnId(instituteID);
        party44Choice.setFIId(fiid);

        return party44Choice;
    }

    public static AppHdr generateMessageHeader() {
        BusinessAppHdrV02 headerParam = new BusinessAppHdrV02();
        headerParam.setBizSvc("swift.cbprplus.01");
        headerParam.setMsgDefIdr("pacs.008.001.08");
        headerParam.setBizMsgIdr("MX00001");
        headerParam.setCreDt(OffsetDateTime.now(ZoneId.of("+00:00")));

        Party44Choice party44Choice = new Party44Choice();
        BranchAndFinancialInstitutionIdentification6 fiid = new BranchAndFinancialInstitutionIdentification6();
        FinancialInstitutionIdentification18 instituteID = new FinancialInstitutionIdentification18();
        instituteID.setBICFI("ABC");
        fiid.setFinInstnId(instituteID);
        party44Choice.setFIId(fiid);

        headerParam.setFr(getParty44ChoiceRandom("BNSCAF001"));
        headerParam.setTo(getParty44ChoiceRandom("BNSCAF002"));

        return headerParam;
    }

    public static void main(String[] args) {
        try {
            // 1. Initialize the main message envelope
            MxPacs00900108 pacs009 = new MxPacs00900108();

            pacs009.setAppHdr(generateMessageHeader());

            FinancialInstitutionCreditTransferV08 fiCdtTrf = new FinancialInstitutionCreditTransferV08();
            pacs009.setFICdtTrf(fiCdtTrf);

            // 2. Build the Group Header (GrpHdr)
            GroupHeader93 grpHdr = new GroupHeader93();
            grpHdr.setMsgId("MSG-ID-2026-0812-XYZ");

            // Set timestamp
            grpHdr.setCreDtTm(OffsetDateTime.now(ZoneId.of("+00:00")));
            grpHdr.setNbOfTxs("1");

            // Set settlement method
            SettlementInstruction7 sttlmInf = new SettlementInstruction7();
            sttlmInf.setSttlmMtd(SettlementMethod1Code.CLRG);
            grpHdr.setSttlmInf(sttlmInf);

            fiCdtTrf.setGrpHdr(grpHdr);

            // 5. Serialize the Java object tree directly to XML string
            MxWriteConfiguration conf = new MxWriteConfiguration();
            conf.useCategoryAsDocumentPrefix = false;   // do not use the category as prefix
            conf.documentPrefix = null;                 // remove the default prefix
            conf.indent = " ";                          // use 1 space for indentation
            conf.includeXMLDeclaration = false;


            String xmlOutput = pacs009.message(conf);
            xmlOutput = xmlOutput.replaceFirst("xmlns:head=\\\"[^\\\"]*\\\"", "");
            xmlOutput = xmlOutput.replaceAll("<(/?)head:", "<$1");
            // Print the XML structure
            System.out.println(xmlOutput);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
