namespace App.Services;

/// <summary>
/// Stocke la session utilisateur après login (token sID).
/// </summary>
public static class SessionManager
{
    public static string? Token { get; set; }
    public static bool IsAdmin { get; set; }
    public static string? CurrentUserEmail { get; set; }

    public static bool IsAuthenticated => !string.IsNullOrEmpty(Token);

    public static void Clear()
    {
        Token = null;
        IsAdmin = false;
        CurrentUserEmail = null;
    }
}
