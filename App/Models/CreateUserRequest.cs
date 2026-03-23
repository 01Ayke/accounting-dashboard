namespace App.Models;

/// <summary>
/// Données pour POST /register (form params).
/// </summary>
public class CreateUserRequest
{
    public string Email { get; set; } = "";
    public string Password { get; set; } = "";
    public string Nom { get; set; } = "";
    public string Prenom { get; set; } = "";
    public string Code { get; set; } = "";
}
