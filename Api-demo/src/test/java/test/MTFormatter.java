package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MTFormatter {
    public static String formatSwiftMT(String singleLineMsg) {
        if (singleLineMsg == null || singleLineMsg.trim().isEmpty()) {
            return "";
        }

        // 1. Clean up outer spaces
        String formatted = singleLineMsg.trim();

        // 2. Put Block 1, Block 2, and Block 3 on their own lines
        formatted = formatted.replaceAll("(\\{1:[^}]+\\})", "$1\n");
        formatted = formatted.replaceAll("(\\{2:[^}]+\\})", "$1\n");
        formatted = formatted.replaceAll("(\\{3:[^}]+\\})", "$1\n");

        // 3. Insert a newline immediately after the Block 4 opening bracket
        formatted = formatted.replace("{4:", "{4:\n");

        // 4. Match and break before Swift tags inside Block 4 (e.g., :20:, :32A:)
        // Regex matches a colon, followed by 2 digits, an optional uppercase letter, and a colon
        String tagRegex = "(:(?:[0-9]{2}[A-Z]?):)";
        formatted = formatted.replaceAll(tagRegex, "\n$1");

        // 5. Ensure the closing sequence of Block 4 and the start of Block 5 are cleanly separated
        formatted = formatted.replace("-}{5:", "\n-}\n{5:");

        // 6. Clean up any accidental double newlines that could form from raw spacing
        formatted = formatted.replaceAll("\n+", "\n");

        return formatted.trim();
    }

    public static void main(String[] args) {
        // A typical raw, single-line MT103 text string
//        String rawMessage = "{1:F01BANKBEBBAXXX0000000000}{2:I103BANKDEFFAXXXN}{3:{108:2K3A01B}}{4::20:REFTX999:23B:CRED:32A:260725EUR50000,:50K:/11223344JOHN DOE:59:/55667788JANE SMITH-}{5:{CHK:123456789ABC}}";
        String rawMessage = """
        {1:F01SENDERDEFFAXXX0000000000}{2:I306RECEIVERXXXXN}{4::15A::20A:TRREF123456:22A:NEWC:94A:BROK:82A:SENDERBICXXX:87A:RECEIVERBICXXX:15B::17A:B:32B:EUR100000:33B:USD95900:36:0.9590:38E:20081002:17F:N:14S:SETT:15C::23E:KOCR:30F:20081002:37R:0.9640}
        -{5:CHECKSUM}
        """;

        System.out.println("--- Raw Message ---");
        System.out.println(rawMessage);
        System.out.println("\n--- Formatted Message ---");

        String result = formatSwiftMT(rawMessage);
        System.out.println(result);
    }
}
