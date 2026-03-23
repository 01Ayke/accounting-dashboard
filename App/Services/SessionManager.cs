namespace App.Services;

/// <summary>
/// Stocke la session utilisateur après login (token sID).
/// </summary>
public static class SessionManager
{
    public static string? Token { get; set; }

    public static bool IsAuthenticated => !string.IsNullOrEmpty(Token);

    public static void Clear()
    {
        Token = null;
    }
}
