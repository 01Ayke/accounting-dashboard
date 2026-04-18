using System.Configuration;
using System.Data;
using System.Windows;

namespace App;

/// <summary>
/// Interaction logic for App.xaml
/// </summary>
public partial class MyApp : Application
{
    protected override void OnStartup(StartupEventArgs e)
    {
        // Fix pour les crashs GLX sous Wine/Linux (ComboBox Popups, effets d'ombre, etc.)
        System.Windows.Media.RenderOptions.ProcessRenderMode = System.Windows.Interop.RenderMode.SoftwareOnly;
        base.OnStartup(e);
    }
}

