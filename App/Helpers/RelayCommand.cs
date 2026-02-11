using System;
using System.Windows.Input;

namespace App.Helpers
{
    /// <summary>
    /// Représente une implémentation simple de <see cref="ICommand"/> permettant de lier
    /// une action à exécuter depuis le ViewModel dans WPF.
    /// </summary>
    /// <remarks>
    /// Cette classe est généralement utilisée dans le pattern MVVM pour déléguer
    /// les commandes des boutons ou autres contrôles UI à des méthodes du ViewModel.
    /// </remarks>
    public class RelayCommand<T> : ICommand
    {
        private readonly Action<T> _execute;
        private readonly Func<T, bool>? _canExecute;

        public RelayCommand(Action<T> execute, Func<T,bool>? canExecute = null)
        {
            _execute = execute ?? throw new ArgumentNullException(nameof(execute));
            _canExecute = canExecute;
        }

        public bool CanExecute(object? parameter) => _canExecute == null || _canExecute((T)parameter!);

        public void Execute(object? parameter) => _execute((T)parameter!);

        public event EventHandler? CanExecuteChanged
        {
            add => CommandManager.RequerySuggested += value;
            remove => CommandManager.RequerySuggested -= value;
        }
    }

}
