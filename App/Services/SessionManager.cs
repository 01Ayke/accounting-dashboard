namespace App.Services;

/// <summary>
/// Stocke la session utilisateur après login (token sID).
/// </summary>
public static class SessionManager
{
    public static string? Token { get; set; }
    public static bool IsAdmin { get; set; }
    public static string? CurrentUserEmail { get; set; }
    public static List<string> UserRoles { get; set; } = new();
    public static int? CurrentDossierId { get; set; }
    public static string? CurrentDossierName { get; set; }

    public static bool IsAuthenticated => !string.IsNullOrEmpty(Token);

    public static bool IsExpert    => UserRoles.Contains("EXPERT");
    public static bool IsComptable => UserRoles.Any(r => r == "COMPTA_CONF" || r == "COMPTA_JR");

    public static void Clear()
    {
        Token = null;
        IsAdmin = false;
        CurrentUserEmail = null;
        UserRoles = new();
        CurrentDossierId = null;
        CurrentDossierName = null;
    }
}
